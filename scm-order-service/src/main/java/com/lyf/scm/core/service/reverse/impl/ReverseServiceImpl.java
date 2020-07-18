package com.lyf.scm.core.service.reverse.impl;

import javax.annotation.Resource;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.reverse.ReverseRecordStatusEnum;
import com.lyf.scm.common.enums.reverse.ReverseRecordTypeEnum;
import com.lyf.scm.common.enums.reverse.ReverseTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.reverse.*;
import com.lyf.scm.core.domain.entity.reverse.ReverseDetailE;
import com.lyf.scm.core.domain.entity.reverse.ReverseE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockQueryFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.reverse.ReverseDetailService;
import com.lyf.scm.core.service.reverse.ReverseOperationToWhRecordService;
import com.rome.arch.core.exception.RomeException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.lyf.scm.core.domain.convert.reverse.ReverseConvert;
import com.lyf.scm.core.domain.convert.reverse.ReverseDetailConvert;
import com.lyf.scm.core.mapper.reverse.ReverseDetailMapper;
import com.lyf.scm.core.mapper.reverse.ReverseMapper;
import com.lyf.scm.core.service.reverse.ReverseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;


/**
 * @Description: 冲销单接口实现对象
 * <p>
 * @Author: wwh 2020/7/16
 */
@Slf4j
@Service("reverseService")
public class ReverseServiceImpl implements ReverseService {
	
	@Resource
	private ReverseMapper reverseMapper;

	@Resource
	private WarehouseRecordMapper warehouseRecordMapper;

	@Resource
	private ReverseDetailMapper reverseDetailMapper;
	
	@Resource
	private ReverseConvert reverseConvert;
	
	@Resource
	private ReverseDetailConvert reverseDetailConvert;

	@Resource
	private OrderUtilService orderUtilService;

	@Resource
	private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;

    @Resource
    private ReverseDetailService reverseDetailService;

    @Resource
    private ItemFacade itemFacade;

    @Resource
    private StockQueryFacade stockQueryFacade;

    @Resource
    private ReverseOperationToWhRecordService reverseOperationToWhRecordService;

    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    /**
     * 创建出入库冲销单
     * @param reverseInfoDTO
     * @return
     */
    @Override
	@Transactional(rollbackFor = Exception.class)
	public String createReverse(ReverseInfoDTO reverseInfoDTO) {
		//生成冲销单据编号
		String recordCode;
		if (StringUtils.isNotBlank(reverseInfoDTO.getRecordCode()) && reverseInfoDTO.getId() != null) {
			recordCode = reverseInfoDTO.getRecordCode();
		} else {
			recordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.OUT_IN_REVERSE.getCode());
		}
		//明细赋值recordCode
		List<ReverseDetailDTO> reverseDetailDTOList = reverseInfoDTO.getReverseDetailDTOList();
		reverseDetailDTOList.stream().forEach((detail) -> detail.setRecordCode(recordCode));
		//保存冲销明细
        reverseDetailService.batchCreateReverseDetail(reverseDetailDTOList,reverseInfoDTO.getUserId());
		if (StringUtils.isNotBlank(reverseInfoDTO.getRecordCode()) && reverseInfoDTO.getId() != null ) {
			// 编辑
			ReverseE reverseEed = reverseMapper.queryByRecordCode(recordCode);
			AlikAssert.isNotNull(reverseEed, ResCode.ORDER_ERROR_8001, ResCode.ORDER_ERROR_8001_DESC + recordCode);
            reverseEed.setModifier(reverseInfoDTO.getUserId());
            reverseEed.setReverseDate(reverseInfoDTO.getReverseDate());
            reverseEed.setRemark(reverseInfoDTO.getRemark());
            reverseEed.setUpdateTime(new Date());
			int i = reverseMapper.updateReverse(reverseEed);
			AlikAssert.isTrue(i >0, ResCode.ORDER_ERROR_8002, ResCode.ORDER_ERROR_8002_DESC + recordCode);

		} else {

            //检验单据类型是否可以创建冲销单
            this.validReverseRecordType(reverseInfoDTO.getOriginRecordCode(),reverseInfoDTO.getRecordType());
            //入库单据类型判断收货单据编号，防止重复创建
            ReverseE reverseE = null;
            if (ReverseTypeEnum.IN_STOCK_REVERSE.getType().equals(reverseInfoDTO.getRecordType())){
                if (StringUtils.isBlank(reverseInfoDTO.getReceiptRecordCode())){
                    throw new RomeException(ResCode.ORDER_ERROR_8003, ResCode.ORDER_ERROR_8003_DESC);
                }
                reverseE = reverseMapper.queryByReceiptRecordCode(reverseInfoDTO.getReceiptRecordCode());
                if (reverseE != null) {
                    throw  new RomeException( ResCode.ORDER_ERROR_8004, ResCode.ORDER_ERROR_8004_DESC + reverseE.getRecordCode());
                }

            }else {
                reverseE = reverseMapper.queryByOriginRecordCode(reverseInfoDTO.getOriginRecordCode());
                if (reverseE != null) {
                    throw  new RomeException( ResCode.ORDER_ERROR_8010, ResCode.ORDER_ERROR_8010_DESC + reverseE.getRecordCode());
                }
            }
			//创建
            reverseE = reverseConvert.convertInfoDTO2E(reverseInfoDTO);
			reverseE.setRecordCode(recordCode);
            reverseE.setOutRecordCode(String.join(",", reverseInfoDTO.getOutRecordCodeList()));
			reverseE.setRecordStatus(ReverseRecordStatusEnum.IS_CREATE.getStatus());
			reverseE.setCreator(reverseInfoDTO.getUserId());
			reverseE.setModifier(reverseInfoDTO.getUserId());
			reverseMapper.insertReverse(reverseE);
		}
		return recordCode;
	}


    @Override
    public PageInfo<ReverseReponseDTO> queryReversePage(QueryReverseDTO queryReverseDTO) {
        Page page = PageHelper.startPage(queryReverseDTO.getPageNum(), queryReverseDTO.getPageSize());
        List<ReverseE> reverseEList = reverseMapper.queryReversePage(queryReverseDTO);
        if (CollectionUtils.isEmpty(reverseEList)) {
            return new PageInfo<>();
        }
        //转换返回对象
        List<ReverseReponseDTO> reverseReponseDTOList = reverseConvert.convertEListToDtoList(reverseEList);
        //查询仓库信息
        List<QueryRealWarehouseDTO> queryRealWarehouseDTOS = new ArrayList<>();
        reverseReponseDTOList.forEach(item -> {
            QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
            queryRealWarehouseDTO.setFactoryCode(item.getFactoryCode());
            queryRealWarehouseDTO.setWarehouseOutCode(item.getRealWarehouseCode());
            queryRealWarehouseDTOS.add(queryRealWarehouseDTO);
        });
        List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseDTOS);
        Map<String, String> realWarehouseInfo = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse::getKey, RealWarehouse::getRealWarehouseCode, (oldValue, newValue) -> oldValue));
        Map<String, String> warehouseInfo = realWarehouseList.stream().collect(Collectors.toMap(RealWarehouse::getKey , RealWarehouse::getRealWarehouseName, (oldValue,newValue) -> oldValue));
        //拼装返回结果
        reverseReponseDTOList.forEach(dto -> {
            //设置仓库信息
            if (realWarehouseInfo.containsKey(dto.getFactoryCode() + "-" + dto.getRealWarehouseCode())) {
                dto.setWarehouseCode(realWarehouseInfo.get(dto.getFactoryCode() + "-" + dto.getRealWarehouseCode()));
            }
            if (warehouseInfo.containsKey(dto.getFactoryCode() + "-" + dto.getRealWarehouseCode())) {
                dto.setRealWarehouseName(warehouseInfo.get(dto.getFactoryCode() + "-" + dto.getRealWarehouseCode()));
            }
        });
        PageInfo<ReverseReponseDTO> pageInfo = new PageInfo<>(reverseReponseDTOList);
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }


    /**
     * 修改状态为已确认
     * @param recordCode userId
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReverseRecordStatusConfirmed(String recordCode, Long userId) {

        // 判断冲销单据编号是否存在
        ReverseE reverseE = reverseMapper.queryByRecordCode(recordCode);
        AlikAssert.isNotNull(reverseE, ResCode.ORDER_ERROR_8001, ResCode.ORDER_ERROR_8001_DESC + recordCode);
        if (ReverseRecordStatusEnum.IS_CREATE.getStatus().equals(reverseE.getRecordStatus())){

            //检验单据类型是否可以冲销单
            this.validReverseRecordType(reverseE.getOriginRecordCode(),reverseE.getRecordType());

            //更新单据状态为已确认状态
            int result = reverseMapper.updateRecordStatusToConfirmedByRecordCode(recordCode,userId);
            if (result == 0) {
                throw new RomeException(ResCode.ORDER_ERROR_8019, ResCode.ORDER_ERROR_8019_DESC);
            }

            //创建出库后置单
            if (ReverseTypeEnum.IN_STOCK_REVERSE.getType().equals(reverseE.getRecordType())){

                //入库冲销单生成出库单
                reverseOperationToWhRecordService.createOutWhRecordByTaskOperation(reverseE);

            }else if (ReverseTypeEnum.OUT_STOCK_REVERSE.getType().equals(reverseE.getRecordType())){

                //出库冲销单生成入库单
                reverseOperationToWhRecordService.createInWhRecordByTaskOperation(reverseE);

            }
        }else if (!ReverseRecordStatusEnum.IS_CONFIRM.getStatus().equals(reverseE.getRecordStatus())){

            throw new RomeException(ResCode.ORDER_ERROR_8007, ResCode.ORDER_ERROR_8007_DESC);
        }


    }

    /**
     * 根据出库单据编码查询出库单(包含明细)
     *
     * @param recordCode
     * @return
     */
    @Override
    public ReverseDTO queryWarehouseRecordByRecordCode(String recordCode) {
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCodeAndBusinessType(recordCode, WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        if (Objects.isNull(warehouseRecordE)){
            throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC+"：出库单不存在" );
        }
        List<WarehouseRecordDetailE> warehouseRecordDetailEList =  warehouseRecordDetailMapper.queryListByRecordCode(recordCode);
        if (CollectionUtils.isEmpty(warehouseRecordDetailEList)){
            throw new RomeException(ResCode.ORDER_ERROR_5112, ResCode.ORDER_ERROR_5112_DESC);
        }
        // 组装冲销单
        ReverseDTO reverseDTO = this.handleWarehouseRecordE(warehouseRecordE);
        // 组装冲销单明细
        List<ReverseDetailDTO> reverseDetailDTOList = this.handleWarehouseRecordDetailE(warehouseRecordDetailEList);
        reverseDTO.setReverseDetailDTOList(reverseDetailDTOList);
        return reverseDTO;
    }

    /**
     * 根据入库单据编码查询收货单(包含明细)
     *
     * @param recordCode        单据编码
     * @param wmsRecordCode     收货单编码
     * @return
     */
    @Override
    public List<ReceiverRecordDTO> queryReceiverRecordByRecordCode(String recordCode, String wmsRecordCode) {
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCodeAndBusinessType(recordCode, WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_1002 , ResCode.ORDER_ERROR_1002_DESC+"：入库单不存在");
        // 从库存中心查收货单
        List<ReceiverRecordDTO> receiverRecordDTOList = stockQueryFacade.getReceiverRecordListByRecordCode(recordCode, wmsRecordCode);
        AlikAssert.isNotNull(receiverRecordDTOList, ResCode.ORDER_ERROR_1001 , "收货单不存在");
        return receiverRecordDTOList;
    }

    /**
     * 组装冲销单
     * @param warehouseRecordE
     * @return
     */
    public ReverseDTO handleWarehouseRecordE(WarehouseRecordE warehouseRecordE){
        List<String> frontRecordCodeList = frontWarehouseRecordRelationMapper.getFrontRecordCodeListByRecordCode(warehouseRecordE.getRecordCode());
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(warehouseRecordE.getRealWarehouseCode(), warehouseRecordE.getFactoryCode());
        if (Objects.isNull(realWarehouse)){
            throw new RomeException(ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
        }
        ReverseDTO reverseDTO = new ReverseDTO();
        reverseDTO.setOriginRecordCode(warehouseRecordE.getRecordCode());
        if (!CollectionUtils.isEmpty(frontRecordCodeList)){
            reverseDTO.setOutRecordCode(StringUtils.join(frontRecordCodeList.toArray(), ","));
        }
        reverseDTO.setFactoryCode(warehouseRecordE.getFactoryCode());
        reverseDTO.setRealWarehouseCode(warehouseRecordE.getRealWarehouseCode());
        reverseDTO.setRealWarehouseName(realWarehouse.getRealWarehouseName());
        return reverseDTO;
    }

    /**
     * 组装冲销单明细
     * @param warehouseRecordDetailEList
     * @return
     */
    public List<ReverseDetailDTO> handleWarehouseRecordDetailE(List<WarehouseRecordDetailE> warehouseRecordDetailEList){
        List<ReverseDetailDTO> reverseDetailDTOList = new ArrayList<>();
        List<String> skuCodeList = warehouseRecordDetailEList.stream().map(WarehouseRecordDetailE::getSkuCode).collect(Collectors.toList());
        //查询商品名称
        List<SkuInfoExtDTO> skuInfoExtDTOList = itemFacade.skuBySkuCodes(skuCodeList);
        //查询单位名称
        List<SkuUnitExtDTO> skuUnitExtDTOList = itemFacade.querySkuUnits(skuCodeList);
        warehouseRecordDetailEList.forEach(detail->{
            ReverseDetailDTO  reverseDetailDTO = new ReverseDetailDTO();
            reverseDetailDTO.setSkuCode(detail.getSkuCode());
            skuInfoExtDTOList.forEach(skuInfoExtDTO -> {if (detail.getSkuCode().equals(skuInfoExtDTO.getSkuCode())){reverseDetailDTO.setSkuName(skuInfoExtDTO.getName());}});
            reverseDetailDTO.setUnitCode(detail.getUnitCode());
            skuUnitExtDTOList.forEach(skuUnitExtDTO -> {if(detail.getUnitCode().endsWith(skuUnitExtDTO.getUnitCode())){reverseDetailDTO.setUnit(skuUnitExtDTO.getUnitName());}});
            reverseDetailDTO.setLineNo(detail.getLineNo());
            reverseDetailDTO.setDeliveryLineNo(detail.getDeliveryLineNo());
            reverseDetailDTO.setReverseQty(detail.getActualQty());
            reverseDetailDTOList.add(reverseDetailDTO);
        });
        return reverseDetailDTOList;
    }


    @Override
    public ReverseDTO queryReverseDetail(String recordCode) {
        ReverseE reverseE = reverseMapper.queryByRecordCode(recordCode);
        if (reverseE == null) {
            return new ReverseDTO();
        }
        ReverseDTO reverseDTO = reverseConvert.convertE2DTO(reverseE);
        //设置仓库名称
        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(reverseDTO.getRealWarehouseCode(),reverseDTO.getFactoryCode());
        if (realWarehouse != null) {
            reverseDTO.setRealWarehouseName(realWarehouse.getRealWarehouseName());
        }
        List<ReverseDetailE> reverseDetailEList = reverseDetailMapper.queryByRecordCode(recordCode);
        if (CollectionUtils.isNotEmpty(reverseDetailEList)) {
            //明细装换
            List<ReverseDetailDTO> reverseDetailDTOList = reverseDetailConvert.convertEList2DTOList(reverseDetailEList);
            //设置商品名称
            List<String> skuCodeList = reverseDetailDTOList.stream().map(ReverseDetailDTO::getSkuCode).collect(Collectors.toList());
            List<SkuInfoExtDTO> skuInfoExtDTOList = itemFacade.skuBySkuCodes(skuCodeList);
            Map<String, SkuInfoExtDTO> skuIdInfos = skuInfoExtDTOList.stream().collect(Collectors.toMap(SkuInfoExtDTO::getSkuCode, Function.identity(), (key1, key2)->key1));
            reverseDetailDTOList.forEach(detail -> {
                if (skuIdInfos.containsKey(detail.getSkuCode())) {
                    detail.setSkuName(skuIdInfos.get(detail.getSkuCode()).getName());
                }
            });
            reverseDTO.setReverseDetailDTOList(reverseDetailDTOList);
        }
        return reverseDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelReverse(String recordCode) {
        if (StringUtils.isBlank(recordCode)) {
            throw new RomeException(ResCode.ORDER_ERROR_8001, ResCode.ORDER_ERROR_8001_DESC);
        }
        ReverseE reverseE = reverseMapper.queryByRecordCode(recordCode);
        AlikAssert.isNotNull(reverseE, ResCode.ORDER_ERROR_8017, ResCode.ORDER_ERROR_8017_DESC);
        Integer recordStatus = reverseE.getRecordStatus();
        if (ReverseRecordStatusEnum.IS_CREATE.getStatus().equals(recordStatus)) {
            return;
        }
        int row = reverseMapper.updateRecordStatusByRecordCode(recordCode,recordStatus);
        AlikAssert.isFalse(row < 1, ResCode.ORDER_ERROR_8019, ResCode.ORDER_ERROR_8019_DESC);
    }

    /**
     * 检验单据类型是否可以创建冲销单
     * @param originRecordCode
     * @param recordType
     */
	private WarehouseRecordE validReverseRecordType(String originRecordCode,Integer recordType){
		//根据单据编号和单据类型查询出入库单
		WarehouseRecordE warehouseRecordE =  warehouseRecordMapper.queryByRecordCodeAndBusinessType(originRecordCode,recordType);
		AlikAssert.isNotNull(warehouseRecordE, ResCode.ORDER_ERROR_8005, ResCode.ORDER_ERROR_8005_DESC );

		//验证类型是否符合冲销类型
		ReverseRecordTypeEnum reverseRecordTypeEnum =  ReverseRecordTypeEnum.queryReverseRecordType(warehouseRecordE.getRecordType());
		AlikAssert.isNotNull(reverseRecordTypeEnum, ResCode.ORDER_ERROR_8006 ,ResCode.ORDER_ERROR_8006_DESC );

		return  warehouseRecordE;

	}

    /**
     * 冲销过账回调
     *
     * @param recordCode  单据编号
     * @param voucherCode SAP凭证号
     * @return
     */
    @Override
    public Boolean reverseSapNotify(String recordCode, String voucherCode) {
        ReverseE reverseE = reverseMapper.queryByRecordCode(recordCode);
        AlikAssert.isNotNull(reverseE, ResCode.ORDER_ERROR_1002 , "冲销单不存在");
        if (ReverseRecordStatusEnum.IS_POST.getStatus().equals(reverseE.getRecordStatus())){
            throw new RomeException(ResCode.ORDER_ERROR_1001, "冲销单已过账");
        }
        reverseE.setRecordStatus(ReverseRecordStatusEnum.IS_POST.getStatus());
        reverseE.setVoucherCode(voucherCode);
        int row = reverseMapper.updateReverse(reverseE);
        if (row != 1){
            throw new RomeException(ResCode.ORDER_ERROR_1001, "冲销过账回调失败");
        }
        return Boolean.TRUE;
    }
}