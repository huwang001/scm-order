package com.lyf.scm.core.service.pack.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.SkuUnitTypeEnum;
import com.lyf.scm.common.enums.pack.DemandRecordStatusEnum;
import com.lyf.scm.common.enums.pack.MoveTypeEnum;
import com.lyf.scm.common.enums.pack.PackCreateTypeEnum;
import com.lyf.scm.common.enums.pack.PackTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.StockNotifyDetailDTO;
import com.lyf.scm.core.api.dto.pack.DemandAllotDTO;
import com.lyf.scm.core.api.dto.pack.DemandAllotDetailDTO;
import com.lyf.scm.core.api.dto.pack.PackDemandComponentDTO;
import com.lyf.scm.core.domain.convert.pack.PackDemandComponentConvertor;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.pack.PackDemandComponentE;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.domain.entity.stockFront.AllotRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.mapper.order.OrderMapper;
import com.lyf.scm.core.mapper.pack.PackDemandComponentMapper;
import com.lyf.scm.core.mapper.pack.PackDemandMapper;
import com.lyf.scm.core.mapper.stockFront.AllotRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WhAllocationDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WhAllocationMapper;
import com.lyf.scm.core.message.PackDemandPickStatusProducer;
import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.pack.PackDemandComponentService;
import com.lyf.scm.core.service.stockFront.WhAllocationService;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 包装需求单明细原料接口实现
 * <p>
 * @Author: wwh 2020/7/7
 */
@Slf4j
@Service("packDemandComponentService")
public class PackDemandComponentServiceImpl implements PackDemandComponentService {

    @Resource
    private PackDemandComponentMapper packDemandComponentMapper;

    @Resource
    private PackDemandMapper packDemandMapper;
    
    @Resource
    private AllotRecordRelationMapper allotRecordRelationMapper;
    
    @Resource
    private WhAllocationMapper whAllocationMapper;
    
    @Resource
    private WhAllocationDetailMapper whAllocationDetailMapper;
    
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private WhAllocationService whAllocationService;
    
    @Resource
    private PackDemandPickStatusProducer packDemandPickStatusProducer;

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Resource
	private ItemFacade itemFacade;

	@Resource
	private BaseFacade baseFacade;
    
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;

    @Resource
    private PackDemandComponentConvertor packDemandComponentConvertor;

	@Value("${app.merchantId}")
    private Long merchantId;

    /**
     * 根据需求编码查询需求单明细原料列表
     *
     * @param recordCode
     * @return
     */
    @Override
    public List<PackDemandComponentDTO> queryDemandComponentByRecordCode(String recordCode) {
    	return this.queryDemandComponentByRecordCodeCommon(recordCode);
    }

    @Override
	public List<PackDemandComponentDTO> queryDemandComponentByRecordCodeAllot(String recordCode) {
    	// 查询调拨单信息
		PackDemandE packDemandE = packDemandMapper.queryByRecordCode(recordCode);
		// 查询渠道信息
		ChannelDTO channelDTO = baseFacade.queryChannelInfoByCode(packDemandE.getChannelCode());
		// 查询仓库
		RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getInRealWarehouseCode(), packDemandE.getInFactoryCode());
		// 获取原料明细
		List<PackDemandComponentDTO> packDemandComponentDtos =  this.queryDemandComponentByRecordCodeCommon(recordCode);
		// 过滤掉不需要领料的原料
		packDemandComponentDtos = packDemandComponentDtos.stream().filter(packDemandComponentDTO -> packDemandComponentDTO.getIsPick() == true).collect(Collectors.toList());
		packDemandComponentDtos.forEach(packDemandComponentDto -> {
			// 加入渠道信息
			if (channelDTO != null) {
				packDemandComponentDto.setChannelCode(channelDTO.getChannelCode());
				packDemandComponentDto.setChannelName(channelDTO.getChannelName());
			}
			// 加入调拨仓库
			if (realWarehouse != null) {
				packDemandComponentDto.setPickWorkshopCode(realWarehouse.getRealWarehouseCode());
			}
			packDemandComponentDto.setPackType(packDemandE.getPackType());
			packDemandComponentDto.setPackTypeName(PackTypeEnum.getDescByType(packDemandComponentDto.getPackType()));
			if(packDemandComponentDto.getLockQty() == null){
				packDemandComponentDto.setLockQty(BigDecimal.ZERO);
			}
			if (packDemandComponentDto.getActualMoveQty() == null){
				packDemandComponentDto.setActualMoveQty(BigDecimal.ZERO);
			}
			packDemandComponentDto.setAllotQty(packDemandComponentDto.getRequireQty().subtract(packDemandComponentDto.getLockQty()).subtract(packDemandComponentDto.getActualMoveQty()));
			if(BigDecimal.ZERO.compareTo(packDemandComponentDto.getAllotQty()) == 1) {
				packDemandComponentDto.setAllotQty(BigDecimal.ZERO);
			}
		});
		return packDemandComponentDtos;
	}

	/**
	 * 公共代码
	 * @return
	 */
	public List<PackDemandComponentDTO> queryDemandComponentByRecordCodeCommon(String recordCode){
		List<PackDemandComponentE> packDemandComponentEs = packDemandComponentMapper.queryDemandComponentByRecordCode(recordCode);
		//查询商品信息
		List<String> shuCodes = packDemandComponentEs.stream().map(PackDemandComponentE::getSkuCode).distinct().collect(Collectors.toList());
		List<SkuInfoExtDTO> skuInfoList = itemFacade.skuBySkuCodes(shuCodes);
		if(CollectionUtils.isNotEmpty(packDemandComponentEs)) {
			//统计需求单明细原料已锁定数量
			this.countLockqty(packDemandComponentEs);
		}
		List<PackDemandComponentDTO> packDemandComponentDtos = new ArrayList<>();
		for(PackDemandComponentE packDemandComponentE : packDemandComponentEs){
			PackDemandComponentDTO packDemandComponentDto = packDemandComponentConvertor.convertE2DTO(packDemandComponentE);
			//设置商品名称
			SkuInfoExtDTO skuInfo = skuInfoList.stream().filter(sku -> sku.getSkuCode().equals(packDemandComponentDto.getSkuCode())).findFirst().orElse(null);
			if (skuInfo != null) {
				packDemandComponentDto.setSkuName(skuInfo.getName());
			}
            packDemandComponentDto.setMoveTypeName(MoveTypeEnum.getDescByType(packDemandComponentDto.getMoveType()));
			packDemandComponentDtos.add(packDemandComponentDto);
		}
		return packDemandComponentDtos;
	}

    /**
     * 根据需求单明细原料创建调拨单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDemandAllot(DemandAllotDTO demandAllotDTO) {
		//根据需求编码查询需求单
		PackDemandE packDemandE = packDemandMapper.queryByRecordCode(demandAllotDTO.getRequireCode());
		if (packDemandE == null) {
		    throw new RomeException(ResCode.ORDER_ERROR_7618, ResCode.ORDER_ERROR_7618_DESC);
		}
		Integer recordStatus = packDemandE.getRecordStatus();
		//recordStatus=0初始 1已确认 2已取消 3部分包装 4已包装完成
		if(!DemandRecordStatusEnum.CONFIRMED.getStatus().equals(recordStatus) && !DemandRecordStatusEnum.PART_PACK.getStatus().equals(recordStatus)) {
			throw new RomeException(ResCode.ORDER_ERROR_7666, ResCode.ORDER_ERROR_7666_DESC);
		}
		//根据需求编码查询需求单明细原料列表
		List<PackDemandComponentE> packDemandComponentEList = packDemandComponentMapper.queryDemandComponentByRecordCode(demandAllotDTO.getRequireCode());
		if (CollectionUtils.isEmpty(packDemandComponentEList)) {
		    throw new RomeException(ResCode.ORDER_ERROR_7704, ResCode.ORDER_ERROR_7704_DESC);
		}
		List<DemandAllotDetailDTO> newDemandAllotDetailList = new ArrayList<DemandAllotDetailDTO>();
		Map<String, PackDemandComponentE> packDemandComponentEMap = packDemandComponentEList.stream().collect(Collectors.toMap(PackDemandComponentE::getKey, Function.identity(), (v1, v2) -> v1));
		demandAllotDTO.getDemandAllotDetailList().forEach(e -> {
		    if (!packDemandComponentEMap.containsKey(e.getParentSkuCode() + "-" + e.getSkuCode())) {
		        //需求单明细原料不包含需求调拨物料
		        throw new RomeException(ResCode.ORDER_ERROR_7663, ResCode.ORDER_ERROR_7663_DESC + "[" + e.getParentSkuCode() + "-" + e.getSkuCode() + "]");
		    } else if (packDemandComponentEMap.containsKey(e.getParentSkuCode() + "-" + e.getSkuCode()) && packDemandComponentEMap.get(e.getParentSkuCode() + "-" + e.getSkuCode()).getIsPick() == true) {
		        //需求单明细原料包含需求调拨物料，并且需求单明细原料isPick=1（已领料）
		        e.setUnit(packDemandComponentEMap.get(e.getParentSkuCode() + "-" + e.getSkuCode()).getUnit());
		        e.setUnitCode(packDemandComponentEMap.get(e.getParentSkuCode() + "-" + e.getSkuCode()).getUnitCode());
		        e.setOrderLineNo(String.valueOf(packDemandComponentEMap.get(e.getParentSkuCode() + "-" + e.getSkuCode()).getId()));
		        newDemandAllotDetailList.add(e);
		    }
		});
		//过滤掉原料明细调拨数量allotQty<=0的数据
		List<DemandAllotDetailDTO> demandAllotDetailList = newDemandAllotDetailList.stream().filter(v -> BigDecimal.ZERO.compareTo(v.getAllotQty()) == -1).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(demandAllotDetailList)) {
		    throw new RomeException(ResCode.ORDER_ERROR_7661, ResCode.ORDER_ERROR_7661_DESC);
		}
		//校验需求调拨明细商品基础单位
		this.checkDemandAllotDetailSkuCodeBaseUnit(demandAllotDetailList);
		//查询入库仓库是否存在
		RealWarehouse inRw = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getInRealWarehouseCode(), packDemandE.getInFactoryCode());
		AlikAssert.isNotNull(inRw, ResCode.ORDER_ERROR_6004, ResCode.ORDER_ERROR_6004_DESC);
		packDemandE.setInRealWarehouse(inRw);
		//查询出库仓库是否存在
		RealWarehouse outRw = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(packDemandE.getOutRealWarehouseCode(), packDemandE.getOutFactoryCode());
		AlikAssert.isNotNull(outRw, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
		packDemandE.setOutRealWarehouse(outRw);
		//预约单销售单号
		packDemandE.setOutVirtualWarehouseCode(null);
		String saleCode = packDemandE.getSaleCode();
		if(StringUtils.isNotBlank(saleCode) && PackCreateTypeEnum.ORDER_CREATE.getType().equals(packDemandE.getCreateType())) {
			//根据销售单号查询预约单
			OrderE orderE = orderMapper.queryOrderBySaleCode(saleCode);
			if(orderE == null) {
				throw new RomeException(ResCode.ORDER_ERROR_1004, ResCode.ORDER_ERROR_1004_DESC);
			}
			packDemandE.setOutVirtualWarehouseCode(orderE.getVmWarehouseCode());
		}
		//根据需求单、原料明细创建调拨单
		whAllocationService.createDemandAllot(packDemandE, demandAllotDetailList);
    }

    /**
     * 校验需求调拨明细商品基础单位
     * 
     * @param demandAllotDetailList
     */
    private void checkDemandAllotDetailSkuCodeBaseUnit(List<DemandAllotDetailDTO> demandAllotDetailList) {
		List<String> skuCodeList = demandAllotDetailList.stream().map(DemandAllotDetailDTO :: getSkuCode).distinct().collect(Collectors.toList());
		List<ParamExtDTO> paramExtDTO = demandAllotDetailList.stream().map(d -> {
			ParamExtDTO paramExt = new ParamExtDTO();
			paramExt.setMerchantId(merchantId);
			paramExt.setSkuCode(d.getSkuCode());
			return paramExt;
		}).collect(Collectors.toList());
		List<SkuUnitExtDTO> skuUnitExts = itemFacade.unitsBySkuCodeAndMerchantId(paramExtDTO);
		Map<String, String> skuBasisUnitExtsMap = skuUnitExts.stream()
				.filter(u -> u.getType().equals(Long.valueOf(SkuUnitTypeEnum.BASIS_UNIT.getId())))
				.collect(Collectors.toMap(SkuUnitExtDTO::getSkuCode, SkuUnitExtDTO::getUnitCode, (v1, v2) -> v1));
		if(skuCodeList.size() < skuBasisUnitExtsMap.size()) {
			throw new RomeException(ResCode.ORDER_ERROR_4010, ResCode.ORDER_ERROR_4010_DESC);
		}
		demandAllotDetailList.forEach(e -> {
			if(!skuBasisUnitExtsMap.containsKey(e.getSkuCode())) {
				throw new RomeException(ResCode.ORDER_ERROR_4011, "商品[" +e.getSkuCode()+ "]不是基本单位");
			}
		});
	}

    /**
     * 统计需求单明细原料已锁定数量
     */
    @Override
	public void countLockqty(List<PackDemandComponentE> packDemandComponentEList) {
		if(CollectionUtils.isEmpty(packDemandComponentEList)) {
			throw new RomeException(ResCode.ORDER_ERROR_7704, ResCode.ORDER_ERROR_7704_DESC);
		}
		String requireCode = packDemandComponentEList.get(0).getRecordCode();
		List<AllotRecordRelationE> allotRecordRelationEList = allotRecordRelationMapper.queryAllotRecordRelationByRecordCode(requireCode);
		List<String> allotCodeList = allotRecordRelationEList.stream().map(AllotRecordRelationE :: getAllotCode).distinct().collect(Collectors.toList());
		List<WhAllocationE> whAllocationEList = new ArrayList<WhAllocationE>();
		if(CollectionUtils.isNotEmpty(allotCodeList)) {
			whAllocationEList = whAllocationMapper.queryNotOutByRecordCodes(allotCodeList);
		}
		List<String> recordList = whAllocationEList.stream().map(WhAllocationE :: getRecordCode).distinct().collect(Collectors.toList());
		List<WhAllocationDetailE> whAllocationDetailEList = new ArrayList<WhAllocationDetailE>();
		if(CollectionUtils.isNotEmpty(recordList)) {
			whAllocationDetailEList = whAllocationDetailMapper.queryDetailByRecordCodes(recordList);
		}
		if(CollectionUtils.isNotEmpty(whAllocationDetailEList)) {
			whAllocationDetailEList.forEach(e -> {
				e.setSkuQty(e.getAllotQty());
			});
			//发货单位向上取整后再转换成基础单位
	        skuQtyUnitTool.convertRealToBasicSku(whAllocationDetailEList, SkuUnitTypeEnum.TRANSPORT_UNIT.getId(), BigDecimal.ROUND_UP);
		}
		Map<Long, WhAllocationDetailE> whAllocationDetailEMap = whAllocationDetailEList.stream().collect(Collectors.toMap(WhAllocationDetailE :: getId, Function.identity(), (v1, v2) -> v1));
		Map<String, List<AllotRecordRelationE>> allotRecordRelationEMap = allotRecordRelationEList.stream().collect(Collectors.groupingBy(AllotRecordRelationE :: getOrderLineNo));
		packDemandComponentEList.forEach(e -> {
			BigDecimal lockQty = BigDecimal.ZERO;
			if(allotRecordRelationEMap.containsKey(String.valueOf(e.getId()))) {
				List<AllotRecordRelationE> list = allotRecordRelationEMap.get(String.valueOf(e.getId()));
				for (AllotRecordRelationE allotRecordRelationE : list) {
					if(whAllocationDetailEMap.containsKey(Long.valueOf(allotRecordRelationE.getLineNo()))) {
						WhAllocationDetailE whAllocationDetailE = whAllocationDetailEMap.get(Long.valueOf(allotRecordRelationE.getLineNo()));
						lockQty = lockQty.add(whAllocationDetailE.getBasicSkuQty());
					}
				}
			}
			e.setLockQty(lockQty);
		});
    }
    
    /**
     * 调拨出库通知
     */
    @Override
	public void allotOutNotify(List<StockNotifyDetailDTO> detailList) {
    	//需求编码
    	String requireCode = null;
    	boolean isSucc = false;
		try {
			List<String> lineNoList = detailList.stream().map(StockNotifyDetailDTO :: getDeliveryLineNo).distinct().collect(Collectors.toList());
			List<AllotRecordRelationE> allotRecordRelationEList = new ArrayList<AllotRecordRelationE>();
			if(CollectionUtils.isNotEmpty(lineNoList)) {
				allotRecordRelationEList = allotRecordRelationMapper.queryAllotRecordRelationByLineNos(lineNoList);
			}
			PackDemandComponentE packDemandComponentE = null;
			if(CollectionUtils.isNotEmpty(allotRecordRelationEList)) {
				String orderLineNo = allotRecordRelationEList.get(0).getOrderLineNo();
				packDemandComponentE = packDemandComponentMapper.queryById(Long.valueOf(orderLineNo));
			}
			if(packDemandComponentE == null) {
				 throw new RomeException(ResCode.ORDER_ERROR_7704, ResCode.ORDER_ERROR_7704_DESC);
			}
			requireCode = packDemandComponentE.getRecordCode();
			//pickStatus=1未领料 2已领料
			int result = packDemandMapper.updatePickStatusByRecordCode(requireCode, 2);
			if(result < 1) {
				throw new RomeException(ResCode.ORDER_ERROR_7664, ResCode.ORDER_ERROR_7664_DESC);
			}
			Map<String, StockNotifyDetailDTO> detailMap = detailList.stream().collect(Collectors.toMap(StockNotifyDetailDTO :: getDeliveryLineNo, Function.identity(), (v1, v2) -> v1));
			allotRecordRelationEList.forEach(e -> {
				if(detailMap.containsKey(e.getLineNo())) {
					if(detailMap.get(e.getLineNo()).getActualQty() == null) {
						e.setActualQty(BigDecimal.ZERO);
					}else {
						e.setActualQty(detailMap.get(e.getLineNo()).getActualQty());
					}
					e.setSkuCode(detailMap.get(e.getLineNo()).getSkuCode());
				}
			});
			allotRecordRelationEList.forEach(e -> {
				int row = packDemandComponentMapper.updateActualMoveQty(e.getOrderLineNo(), e.getSkuCode(), e.getActualQty());
				if(row < 1) {
					throw new RomeException(ResCode.ORDER_ERROR_7665, ResCode.ORDER_ERROR_7665_DESC +"：行信息错误[" + e.getLineNo() + "]，商品编码[" + e.getSkuCode() +"]");
				}
			});
			isSucc = true;
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			if(!isSucc) {
				//MQ通知包装系统更新需求单领料状态
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requireCode", requireCode);
				//pickStatus=1未领料 2已领料
				jsonObject.put("pickStatus", 2);
				packDemandPickStatusProducer.sendAsyncMQ(jsonObject);
			}
		}
	}
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSavePackDemandComponent(List<PackDemandComponentDTO> packDemandComponentDTOList, Long userId) {
        if (CollectionUtils.isEmpty(packDemandComponentDTOList)) {
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }

        //查询 单位转化 信息
        List<String> skuCodeList = packDemandComponentDTOList.stream().map(PackDemandComponentDTO::getSkuCode).distinct().collect(Collectors.toList());
        List<SkuUnitExtDTO> skuUnitExtList = itemFacade.querySkuUnits(skuCodeList);
        Map<String, List<SkuUnitExtDTO>> skuCode2UnitList = skuUnitExtList.stream().collect(Collectors.groupingBy(skuUnit -> skuUnit.getSkuCode()));
        //skuCode 运输单位 信息
        Map<String, BigDecimal> skuCode2transPortUnitMap = new HashMap<>();
        for(String skuCode : skuCode2UnitList.keySet()) {
            SkuUnitExtDTO transPortUnit = skuCode2UnitList.get(skuCode).stream().filter(unit ->
                    SkuUnitTypeEnum.TRANSPORT_UNIT.getId().equals(unit.getType())).findFirst().orElse(null);
            if(null == transPortUnit) {
                continue;
            }
            skuCode2transPortUnitMap.put(skuCode, transPortUnit.getScale());
        }
        List<PackDemandComponentE> packDemandComponentEs = packDemandComponentConvertor.convertDTOList2EList(packDemandComponentDTOList);
        packDemandComponentEs.stream().forEach(packDemandComponentE ->{
            if(skuCode2transPortUnitMap.containsKey(packDemandComponentE.getSkuCode())) {
                packDemandComponentE.setBoxUnitRate(skuCode2transPortUnitMap.get(packDemandComponentE.getSkuCode()));
            }
            packDemandComponentE.setCreator(userId);
            packDemandComponentE.setModifier(userId);
            if(packDemandComponentE.getActualMoveQty() == null){
				packDemandComponentE.setActualMoveQty(BigDecimal.ZERO);
			}
            // 计算需求箱单位数量。公式：需求箱单位数量=需求数量/箱单位换算率 (向上取整)
            if (packDemandComponentE.getRequireQty() != null && packDemandComponentE.getRequireQty().compareTo(BigDecimal.ZERO) > 0
                    && packDemandComponentE.getBoxUnitRate() != null && packDemandComponentE.getBoxUnitRate().compareTo(BigDecimal.ZERO) > 0){
                BigDecimal requireBoxQty = packDemandComponentE.getRequireQty().divide(packDemandComponentE.getBoxUnitRate(), 0, BigDecimal.ROUND_UP);
                packDemandComponentE.setRequireBoxQty(requireBoxQty);
            } else {
                packDemandComponentE.setRequireBoxQty(BigDecimal.ZERO);
            }
        });
		String requireCode = packDemandComponentEs.get(0).getRecordCode();
		packDemandComponentMapper.deleteByRecordCode(requireCode);
		// 新增
		int row = packDemandComponentMapper.batchInsertPackDemandComponent(packDemandComponentEs);
        if (packDemandComponentDTOList.size() != row) {
            throw new RomeException(ResCode.ORDER_ERROR_1001, ResCode.ORDER_ERROR_1001_DESC);
        }
    }

    /**
     * 同步需求单领料状态给包装系统
     */
	@Override
	public void asyncPickStatus(String requireCode, Integer pickStaus) {
		//MQ通知包装系统更新需求单领料状态
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("requireCode", requireCode);
		//pickStatus=1未领料 2已领料
		jsonObject.put("pickStatus", pickStaus);
		packDemandPickStatusProducer.sendAsyncMQ(jsonObject);
	}

}