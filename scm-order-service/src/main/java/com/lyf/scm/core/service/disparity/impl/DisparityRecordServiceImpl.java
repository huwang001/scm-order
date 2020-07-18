package com.lyf.scm.core.service.disparity.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.lyf.scm.common.enums.StockCoreConsts;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.api.dto.disparity.BatchRefusedBackDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityDetailDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityRecordDetailDTO;
import com.lyf.scm.core.api.dto.disparity.QueryDisparityDTO;
import com.lyf.scm.core.common.RomeCollectionUtil;
import com.lyf.scm.core.common.constant.DisparityResponsibleTypeVO;
import com.lyf.scm.core.common.constant.DisparityStatusVO;
import com.lyf.scm.core.domain.convert.disparity.DisparityConvertor;
import com.lyf.scm.core.domain.entity.disparity.DisparityDetailE;
import com.lyf.scm.core.domain.entity.disparity.DisparityRecordE;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.RealWarehouseE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.model.disparity.DisparityDetailDO;
import com.lyf.scm.core.domain.model.disparity.DisparityRecordDO;
import com.lyf.scm.core.mapper.disparity.DisparityDetailMapper;
import com.lyf.scm.core.mapper.disparity.DisparityRecordMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.BatchResultDTO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockDisparityRecordFacade;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.trade.dto.UpdatePoDTO;
import com.lyf.scm.core.service.disparity.DisparityRecordService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: DisparityRecordServiceImpl
 * @Description: 差异订单-服务
 * @author: Lin.Xu
 * @date: 2020-7-10 19:57:24
 * @version: v1.0
 */
@Slf4j
@Service
public class DisparityRecordServiceImpl implements DisparityRecordService {

    private final ParamValidator validator = ParamValidator.INSTANCE;

    @Resource
    private DisparityConvertor disparityConvertor;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private DisparityDetailMapper disparityDetailMapper;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private DisparityRecordMapper disparityRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private StockDisparityRecordFacade stockDisparityRecordFacade;
    @Resource
    private WarehouseRecordService warehouseRecordService;


    /**
     * 查询差异单
     * @param paramDTO
     * @return
     */
    @Override
    public PageInfo<DisparityRecordDetailDTO> queryByCondition(QueryDisparityDTO paramDTO) {
        //查询出入实仓id
        if (validator.validStr(paramDTO.getInRealWarehouseCode())) {
            RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(paramDTO.getInRealWarehouseCode());
            if (realWarehouse != null) {
                paramDTO.setInWarehouseId(realWarehouse.getId());
            } else {
                return new PageInfo<>(new ArrayList<>());
            }
        }
        if (validator.validStr(paramDTO.getOutRealWarehouseCode())) {
            RealWarehouse realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByRWCode(paramDTO.getOutRealWarehouseCode());
            if (realWarehouse != null) {
                paramDTO.setOutWarehouseId(realWarehouse.getId());
            } else {
                return new PageInfo<>(new ArrayList<>());
            }
        }

        if (validator.validStr(paramDTO.getFactoryCode())) {
            List<RealWarehouse> realWarehouse = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodesNoShop(paramDTO.getFactoryCode());
            if (realWarehouse != null && realWarehouse.size() > 0) {
                List<Long> rwIds = RomeCollectionUtil.getValueList(realWarehouse, "id");
                if (FrontRecordTypeEnum.DIRECT_SHOP_REPLENISH_DISPARITY_RECORD.getType().equals(paramDTO.getRecordType())) {
                    paramDTO.setOutRealWarehouseIds(rwIds);
                } else {
                    paramDTO.setInRealWarehouseIds(rwIds);
                }
            } else {
                return new PageInfo<>(new ArrayList<>());
            }
        }
        if (StringUtils.isBlank(paramDTO.getSapDeliveryCode())) {
            paramDTO.setSapDeliveryCodeList(null);
        } else {
            //忽略换行符
            String replaceStr = paramDTO.getSapDeliveryCode().replaceAll("\\n|\\r", ",");
            String[] split = replaceStr.split(",");
            List<String> sapDeliveryCodes = Arrays.asList(split);
            sapDeliveryCodes = sapDeliveryCodes.stream().distinct().collect(Collectors.toList());
            AlikAssert.isTrue(sapDeliveryCodes.size() <= 50, ResCode.ORDER_ERROR_1003, ResCode.STOCK_ERROR_1002_DESC + ":交货单号不能超过50");
            paramDTO.setSapDeliveryCodeList(sapDeliveryCodes);
        }
        Page<?> page = PageHelper.startPage(paramDTO.getPageIndex(), paramDTO.getPageSize());
        List<DisparityRecordDetailDTO> DisparityRecordDetailDTOList = disparityRecordMapper.selectByCondition(paramDTO);
        PageInfo<DisparityRecordDetailDTO> pageInfo = new PageInfo<>(DisparityRecordDetailDTOList);
        pageInfo.setTotal(page.getTotal());
        List<Long> rwIds = new ArrayList<>();
        List<QueryRealWarehouseDTO> queryRealWareList = new CopyOnWriteArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (DisparityRecordDetailDTO result : DisparityRecordDetailDTOList) {
            rwIds.add(result.getInRealWarehouseId());
            rwIds.add(result.getOutRealWarehouseId());
            if (null != result.getHandlerInRealWarehouseId()) {
                rwIds.add(result.getHandlerInRealWarehouseId());
                QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
                queryRealWarehouseDTO.setFactoryCode(result.getInFactoryCode());
                queryRealWarehouseDTO.setWarehouseOutCode(result.getInRealWarehouseCode());
                queryRealWareList.add(queryRealWarehouseDTO);
            }
            if (!skuIds.contains(result.getSkuId())) {
                skuIds.add(result.getSkuId());
            }
        }
        List<RealWarehouse> rwList = stockRealWarehouseFacade.queryByOutCodeAndFactoryCodeList(queryRealWareList);
        Map<Long, RealWarehouseE> rwMap = RomeCollectionUtil.listforMap(rwList, "id", null);
        List<SkuInfoExtDTO> skuInfoList = null;
        try {
            skuInfoList = itemFacade.skusBySkuId(skuIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Map<Long, SkuInfoExtDTO> skuInfoMap = RomeCollectionUtil.listforMap(skuInfoList, "id", null);
        DisparityRecordDetailDTOList.forEach(result -> {
            RealWarehouseE inRealWarehouseE = rwMap.get(result.getInRealWarehouseId());
            RealWarehouseE outRealWarehouseE = rwMap.get(result.getOutRealWarehouseId());
            if (null != inRealWarehouseE) {
                result.setInRealWarehouseName(inRealWarehouseE.getRealWarehouseName());
                result.setInRealWarehouseCode(inRealWarehouseE.getRealWarehouseCode());
                result.setInFactoryCode(inRealWarehouseE.getFactoryCode());
                if (RealWarehouseTypeEnum.RW_TYPE_1.getType().equals(inRealWarehouseE.getRealWarehouseType())) {
                    result.setShopCode(inRealWarehouseE.getShopCode());
                }
            }
            if (null != outRealWarehouseE) {
                result.setOutRealWarehouseName(outRealWarehouseE.getRealWarehouseName());
                result.setOutRealWarehouseCode(outRealWarehouseE.getRealWarehouseCode());
                result.setOutFactoryCode(outRealWarehouseE.getFactoryCode());
                if (RealWarehouseTypeEnum.RW_TYPE_1.getType().equals(outRealWarehouseE.getRealWarehouseType())) {
                    result.setShopCode(outRealWarehouseE.getShopCode());
                }
            }
            if (null != result.getHandlerInRealWarehouseId()) {
                RealWarehouseE handlerInRealWarehouseE = rwMap.get(result.getHandlerInRealWarehouseId());
                if (null != handlerInRealWarehouseE) {
                    result.setHandlerInRealWarehouseName(handlerInRealWarehouseE.getRealWarehouseName());
                    result.setHandlerInFactoryCode(handlerInRealWarehouseE.getFactoryCode());
                    result.setHandlerInRealWarehouseCode(handlerInRealWarehouseE.getRealWarehouseCode());
                }
            }
            if (FrontRecordTypeEnum.DIRECT_SHOP_RETURN_DISPARITY_RECORD.getType().equals(result.getRecordType())) {
                if (null != inRealWarehouseE && !DisparityStatusVO.INIT.getStatus().equals(result.getRecordStatus())) {
                    result.setHandlerInRealWarehouseName(inRealWarehouseE.getRealWarehouseName());
                    result.setHandlerInRealWarehouseCode(inRealWarehouseE.getRealWarehouseCode());
                    result.setHandlerInFactoryCode(inRealWarehouseE.getFactoryCode());

                }
            }
            SkuInfoExtDTO skuInfoDTO = skuInfoMap.get(result.getSkuId());
            if (null != skuInfoDTO) {
                result.setSkuName(skuInfoDTO.getName());
            }
            if (null != result.getScale()) {
                BigDecimal realQty = result.getSkuQty().divide(result.getScale(), StockCoreConsts.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
                BigDecimal realInQty = result.getInSkuQty().divide(result.getScale(), StockCoreConsts.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
                BigDecimal realOutQty = result.getOutSkuQty().divide(result.getScale(), StockCoreConsts.DECIMAL_POINT_NUM, BigDecimal.ROUND_DOWN);
                result.setRealSkuQty(realQty);
                result.setRealInSkuQty(realInQty);
                result.setRealOutSkuQty(realOutQty);
            }
        });
        return pageInfo;
    }

	/**
	 * T:添加保存差异订单信息
	 * 	1.校验入参信息
	 * 	2.查询后置出库记录及出库记录明细
	 * 	3.查询后置入库记录及入库记录明细
	 * 	4.通过出库记录比较入库记录之间的出入数量差异生成差异明细信息
	 * 	5.通过后置出入单及其他信息生成差异记录
	 * @author Lin.Xu
	 * @date 2020年7月11日09:25:01
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public void addDisparityRecord(String recordCode, List<FrontWarehouseRecordRelationE> frontRecordList) {
		//1.校验入参信息,建立入库单号和出库单号
		AlikAssert.isNotBlank(recordCode, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"单据编码不能为空");
		if(CollectionUtils.isEmpty(frontRecordList)) {
			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"前置单据信息为空");
		}
		String putInStockNO = recordCode;
		String outStockNO = disparityRecordMapper.selectOutStockNoByPutInNo(putInStockNO);
		AlikAssert.isNotBlank(outStockNO, ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"出库单据编码不能为空");
		//1.1判断前置单类型(差异订单支持：直营门店补货单、门店冷链交货单)
		FrontWarehouseRecordRelationE frontWarehouseRecordRelationE = frontRecordList.get(0);
		Integer frontRecordType = frontRecordList.get(0).getFrontRecordType();
		if (!FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType().equals(frontRecordType)
                && !FrontRecordTypeEnum.SHOP_COLD_CHAIN_DELIVERY_RECORD.getType().equals(frontRecordType)
                && !FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)
                && !FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType().equals(frontRecordType)) {
			return ;
	    }
		//2.查询后置出库记录及出库记录明细
		WarehouseRecordE inWarehouseRecordE = warehouseRecordMapper.queryByRecordCode(putInStockNO);
		AlikAssert.isNotNull(inWarehouseRecordE,  ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"入库单据信息不存在");
		List<WarehouseRecordDetailE> inWarehouseDetailList = warehouseRecordDetailMapper.queryListByRecordCode(putInStockNO);
		AlikAssert.isNotNull(inWarehouseDetailList,  ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"入库单据明细信息不存在");
		//3.查询后置入库记录及入库记录明细
		WarehouseRecordE outWarehouseRecordE = warehouseRecordMapper.queryByRecordCode(outStockNO);
		AlikAssert.isNotNull(outWarehouseRecordE,  ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"出库单据信息不存在");
		List<WarehouseRecordDetailE> outWarehouseDetailList = warehouseRecordDetailMapper.queryListByRecordCode(outStockNO);
		AlikAssert.isNotNull(outWarehouseDetailList,  ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002+":"+"出库单据明细信息不存在");
		//4.通过出库记录比较入库记录之间的出入数量差异生成差异明细信息
		Map<String, WarehouseRecordDetailE> inWarehouseDetailListMap = inWarehouseDetailList.stream().collect(Collectors.toMap(WarehouseRecordDetailE::getDeliveryLineNo, Function.identity()));
		Map<String, WarehouseRecordDetailE> outWarehouseDetailListMap = outWarehouseDetailList.stream().collect(Collectors.toMap(WarehouseRecordDetailE::getDeliveryLineNo, Function.identity()));
		//5.构建差异订单出入库所有的DeliveryLineNo单号
		Set<String> unionDlLineNoList = (Set<String>) CollectionUtils.union(outWarehouseDetailListMap.keySet(), inWarehouseDetailListMap.keySet());
		List<DisparityDetailDO> disparityDetailEs = new ArrayList<DisparityDetailDO>();
		//5.1出入库都有的订单明细
		for(String deliveryLineNo : unionDlLineNoList) {
			WarehouseRecordDetailE outWarehouseRecordDetailE = outWarehouseDetailListMap.get(deliveryLineNo);
			WarehouseRecordDetailE inWarehouseRecordDetailE = inWarehouseDetailListMap.get(deliveryLineNo);
			//根据场景进行构建明细
			DisparityDetailDO disparityDetailDO = DisparityBeanUtil.createDisparityDetailDO(inWarehouseRecordDetailE, outWarehouseRecordDetailE, frontWarehouseRecordRelationE);
			if(null != disparityDetailDO) {
				disparityDetailEs.add(disparityDetailDO);
			}
		}
		//6.判断是否存在差异信息，进行构建差异单记录
		if(CollectionUtils.isNotEmpty(disparityDetailEs)) {
            //调用订单号生成
            String disparityRecordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.getCodeByType(frontRecordType));
            DisparityRecordDO disparityRecordDo = DisparityBeanUtil.createDisparityRecordDO(disparityRecordCode, inWarehouseRecordE, outWarehouseRecordE, frontWarehouseRecordRelationE, FrontRecordTypeEnum.getEnumByType(frontRecordType));
            if(null == disparityRecordDo) {
            	throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003+":"+"构建差异订单记录失败");
            }
            //entity对象转换
            int saveNum = disparityRecordMapper.isnert(disparityRecordDo);
            if (1 != saveNum) {
                throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003 + ":" + "插入差异订单记录失败");
            }
            Long disparityRecordId = disparityRecordDo.getId();
            //插入明细信息
            disparityDetailEs.forEach(dr -> {
                dr.setDisparityId(disparityRecordId);
            });
            disparityDetailMapper.insertBatch(disparityDetailEs);
        }
    }

    /**
     * T:批量整单拒收
     */
    @Override
    public List<BatchRefusedBackDTO> overallRejection(List<String> putInNos, Long modifier) {
        //发起批量拒收数据
        List<BatchResultDTO> resultList = stockDisparityRecordFacade.rejectShopReceipt(putInNos);
        //转换对象返回
        return disparityConvertor.brDtoTobackDtoList(resultList);
    }

    /**
     * T:差异定责
     * 1.校验差异保存信息
     * 2.批量保存定责原因
     */
    @Override
    public void disparityDuty(List<DisparityDetailDTO> details) {
        //1.校验差异保存信息
        for (DisparityDetailDTO dddto : details) {
            if (null == dddto.getId()) {
                throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002 + ":" + "定责记录ID不能为空");
            }
        }
        //2.批量保存
        List<DisparityDetailE> disparityDetailEs = disparityConvertor.dtoToEntityForList(details);
        int saveNum = disparityDetailMapper.updateReasonsById(disparityDetailEs);
        if (saveNum <= 0) {
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003 + ":" + "批量差异定责修改库数据失败");
        }
    }


    /**
     * 确认过账处理逻辑：
     * 1针对差异单明细修改状态为过账中
     * 2根据差异单明细和判责 生成对应的出入库单，然后同步给库存中心
     * @param detailsIds
     * @param modifier
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void confirmPosting(List<Long> detailsIds, Long modifier) {
        if (modifier == null) {
            modifier = 0L;
        }
        disparityRecordMapper.updateToWait(detailsIds, modifier);
        //处理差异单，并将明细状态更新为处理成功
        determineResponsibility(detailsIds, modifier);
    }

    /**
     * @Description 门店补货PO推送交易
     * @Author Lin.Xu
     * @Date 16:39 2020/7/17
     * @Param []
     * @return void
     **/
    @Override
    public void pushTransactionStatus() {
        //分页查询查询待推送的入库后置单信息
        int page, currentPage = 1, maxResult = 100;
        List<WarehouseRecordE> recordES;
        do{
            page = (currentPage - 1) * maxResult;
            recordES = warehouseRecordMapper.queryAllBySyncTradeStatus(page, maxResult);
            //有可能第一次循环就没有数据
            if(null == recordES || recordES.size() == 0){
                break;
            }
            //循环进行封装参数处理
            for(WarehouseRecordE recordE : recordES){
                UpdatePoDTO poUpdateQuantityDTO = new UpdatePoDTO();
                List<FrontWarehouseRecordRelationE> frontWarehouseRecordRelationES = frontWarehouseRecordRelationMapper.getByWrId(recordE.getId());
                if(CollectionUtils.isEmpty(frontWarehouseRecordRelationES)){

                }
                //poUpdateQuantityDTO.setDoNo(disparityRecordE.getInWarehouseRecordCode());
            }
        }while (recordES.size() == maxResult);
    }



    /**
     * @Description 判定明细责任，处理差异单逻辑：
     * 1首先将差异单集合按：差异单号+责任类型+出入库仓 依次分类，目的在于同步给库存中心时按这三个维度分类处理
     * 2根据判责的结果做对应处理，此逻辑主要是关注当库存存在差异后如何处理让库存保持一致 ，业务逻辑：
     *  2.1门店责任，针对门店生成一条入库单
     *  2.2仓库责任，针对仓库生成一条入库单
     *  2.3物流责任，针对仓库生成一条入库单，然后再针对仓库生成一条对物流的出库单
     * 3更新差异单明细的状态为处理完成
     * 4将生成的出入库单同步给库存中心，从财务角度的平账是库存中心与sap的交互
     * 5库存中心返回成功则结束，失败或者超时 则调用库存中心取消接口，然后再回滚。（
     * T注：调用取消接口的目的是针对一个差异单明细，每次点击确认过账按钮都会重新生成出入库单号，所以库存中心的同步接口没法支持幂等性）
     * @Author Lin.Xu
     * @Date 19:34 2020/7/16
     * @Param [detailsIds, modifier]
     * @return void
     **/
    private void determineResponsibility(List<Long> detailsIds, Long modifier){
        //1.通过明细ID获取明细信息
        List<DisparityDetailE> disparityDetailList = disparityDetailMapper.selectDisparityDetailByIds(detailsIds);
        if(CollectionUtils.isEmpty(disparityDetailList)){
            throw new RomeException(ResCode.ORDER_ERROR_1002_DESC, ResCode.ORDER_ERROR_1002+":"+"未找到需要带确认过账数据");
        }
        //定义入库和出库编号两个集合
        List<String> putInRearOrderNoList = new ArrayList<String>();
        List<String> outRearOrderNoList = new ArrayList<String>();
        //2.获取差异记录，按照差异单进行分组(即：按照disparityId进行分组)，然后按照订单进行分组
        Map<Long, List<DisparityDetailE>> disparityDetailMapByDisId = RomeCollectionUtil.listforListMap(disparityDetailList, "disparityId");
        for(Long disparityIdKey : disparityDetailMapByDisId.keySet()){
            //3.根据单据类型进行判断是否需要进行过账
            DisparityRecordE disparityRecordE = disparityRecordMapper.selectDisparityRecordById(disparityIdKey);
            AlikAssert.isNotNull(disparityRecordE,ResCode.ORDER_ERROR_7101, "差异单编号" + disparityIdKey + "信息不存在");
            Integer recordType = disparityRecordE.getRecordType();
            if(FrontRecordTypeEnum.DIRECT_SHOP_REPLENISH_DISPARITY_RECORD.getType().equals(recordType)){
                //4.每个出入单的差异单集合按照责任判断进行分类
                List<DisparityDetailE> dsIdDisparityDetailList = disparityDetailMapByDisId.get(disparityIdKey);
                Map<Integer, List<DisparityDetailE>> disparityDetailMapByZeRenType = RomeCollectionUtil.listforListMap(dsIdDisparityDetailList, "responsibleType");
                for(Integer zeRenType : disparityDetailMapByZeRenType.keySet()){
                    //4.1门店责任，通知交易
                    if(DisparityResponsibleTypeVO.SHOP.getType().equals(zeRenType)){
                        List<DisparityDetailE> shopDisparityDetailList = disparityDetailMapByZeRenType.get(zeRenType);
                        WarehouseRecordTypeEnum warehouseRecordTypeEnum = WarehouseRecordTypeEnum.DISPARITY_SHOP_IN_RECOED11;
                        String orderNo = orderUtilService.queryOrderCode(warehouseRecordTypeEnum.getCode());
                        //4.1.1由于按照差异单记录，说明这里是同一个门店仓不需要按仓在分组
                        WarehouseRecordE warehouseRecordE = DisparityBeanUtil.createWarehouseRecordE(orderNo,shopDisparityDetailList,
                                disparityRecordE,warehouseRecordTypeEnum,WarehouseRecordConstant.NEED_SYNC_TRADE);
                        //4.1.2插入后置单
                        saveWarehouseRecordE(disparityRecordE, warehouseRecordE);
                        //订单号加入到入库单集合中
                        putInRearOrderNoList.add(orderNo);
                    //4.2仓库责任，无需通知交易
                    }else if(DisparityResponsibleTypeVO.WAREHOUSE.getType().equals(zeRenType)){
                        List<DisparityDetailE> warehouseDisparityDetailList = disparityDetailMapByZeRenType.get(zeRenType);
                        WarehouseRecordTypeEnum warehouseRecordTypeEnum = WarehouseRecordTypeEnum.DISPARITY_TRANSFER_WAREHOUSE_IN_RECOED12;
                        String orderNo = orderUtilService.queryOrderCode(warehouseRecordTypeEnum.getCode());
                        //4.2.1由于按照差异单记录，说明这里是同一个门店仓不需要按仓在分组
                        WarehouseRecordE warehouseRecordE = DisparityBeanUtil.createWarehouseRecordE(orderNo,warehouseDisparityDetailList,
                                disparityRecordE,warehouseRecordTypeEnum,WarehouseRecordConstant.INIT_SYNC_TRADE);
                        //4.2.2插入后置单
                        saveWarehouseRecordE(disparityRecordE, warehouseRecordE);
                        //订单号加入到入库单集合中
                        putInRearOrderNoList.add(orderNo);
                    //4.3物流责任
                    }else if(DisparityResponsibleTypeVO.LOGISTIC.getType().equals(zeRenType)){
                        List<DisparityDetailE> logisticDisparityDetailList = disparityDetailMapByZeRenType.get(zeRenType);
                        //4.3.1按照选定的仓库进行出入单据创建，按照仓库分类，一进一出
                        Map<String, List<DisparityDetailE>> logisticDisparityDetailMap = RomeCollectionUtil.listforListMap(logisticDisparityDetailList, "handlerInRealWarehouseOutCode");
                        for(String outCodeKey : logisticDisparityDetailMap.keySet()){
                            List<DisparityDetailE> logisticDisparityDetails = logisticDisparityDetailMap.get(outCodeKey);
                            //入库
                            WarehouseRecordTypeEnum wrPutInTypeEnum = WarehouseRecordTypeEnum.DISPARITY_TRANSFER_WAREHOUSE_IN_RECOED13;
                            String putInOrderNo = orderUtilService.queryOrderCode(wrPutInTypeEnum.getCode());
                            WarehouseRecordE putInWarehouseRecordE = DisparityBeanUtil.createWarehouseRecordE(putInOrderNo,logisticDisparityDetails,
                                    disparityRecordE, wrPutInTypeEnum, WarehouseRecordConstant.INIT_SYNC_TRADE);
                            saveWarehouseRecordE(disparityRecordE, putInWarehouseRecordE);
                            //订单号加入到入库单集合中
                            putInRearOrderNoList.add(putInOrderNo);
                            //出库
                            WarehouseRecordTypeEnum wrOutTypeEnum = WarehouseRecordTypeEnum.DISPARITY_TRANSFER_WAREHOUSE_OUT_RECOED13;
                            String outOrderNo = orderUtilService.queryOrderCode(wrOutTypeEnum.getCode());
                            WarehouseRecordE outWarehouseRecordE = DisparityBeanUtil.createWarehouseRecordE(outOrderNo, logisticDisparityDetails,
                                    disparityRecordE, wrOutTypeEnum, WarehouseRecordConstant.INIT_SYNC_TRADE);
                            saveWarehouseRecordE(disparityRecordE, outWarehouseRecordE);
                            //订单号加入到入库单集合中
                            outRearOrderNoList.add(outOrderNo);
                        }
                    }else{
                        log.warn(zeRenType+"责任类型不在过账处理范围");
                    }
                }
            }else{
                //其他类型不过账
            }
        }
        //WRITE DEV 最后进行调用库存中心进行过账  这里需要记录日志
        sendStockBatchPosting(putInRearOrderNoList, outRearOrderNoList);
    }

    /**
     * @Description 保存后置单据数据和前置之间关系数据
     * @Author Lin.Xu
     * @Date 22:39 2020/7/16
     * @Param [disparityRecordE, warehouseRecordE]
     * @return void
     **/
    private void saveWarehouseRecordE(DisparityRecordE disparityRecordE, WarehouseRecordE warehouseRecordE){
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecordE);
        //4.1.2插入后置单明细
        warehouseRecordE.getWarehouseRecordDetailList().forEach(record -> {
            record.setWarehouseRecordId(warehouseRecordE.getId());
            record.setRecordCode(warehouseRecordE.getRecordCode());
            record.setRealWarehouseId(warehouseRecordE.getRealWarehouseId());
            record.setChannelCode(warehouseRecordE.getChannelCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordE.getWarehouseRecordDetailList());
        //查询前置单信息-创建新的后置单与前置单之间关联关系
        List<FrontWarehouseRecordRelationE> frontWarehouseRecordRelationES = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(disparityRecordE.getInWarehouseRecordCode());
        if(CollectionUtils.isEmpty(frontWarehouseRecordRelationES)){
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC + ":获取前置关联关系单为空:"+disparityRecordE.getFrontRecordCode());
        }
        FrontWarehouseRecordRelationE frontRecord = frontWarehouseRecordRelationES.get(0);
        //获取前置单关联信息
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecordE.getId());
        relation.setFrontRecordId(frontRecord.getFrontRecordId());
        relation.setFrontRecordType(warehouseRecordE.getRecordType());
        relation.setRecordCode(warehouseRecordE.getRecordCode());
        relation.setFrontRecordCode(frontRecord.getFrontRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    /**
     * @Description 批量推送入库信息到库存中心进行过账
     * @Author Lin.Xu
     * @Date 11:39 2020/7/17
     * @Param [putInRearOrderNoList, outRearOrderNoList]
     * @return void
     **/
    private void sendStockBatchPosting(List<String> putInRearOrderNoList, List<String> outRearOrderNoList){
        //查询出入库单集合信息
        List<WarehouseRecordE> putInWarehouseRecordES = warehouseRecordService.queryWarehouseRecordByOrderNos(putInRearOrderNoList);
        List<WarehouseRecordE> outWarehouseRecordES = warehouseRecordService.queryWarehouseRecordByOrderNos(outRearOrderNoList);
        //查询入库单对应的叫货时的出货单编码
        Map<String, String> outOrderNoMap = new HashMap<String, String>();
        for(String putInNo : putInRearOrderNoList){
            //查询出库单号
            String outStockNO = disparityRecordMapper.selectOutStockNoByPutInNo(putInNo);
            if(StringUtils.isNotEmpty(outStockNO)){
                outOrderNoMap.put(putInNo, outStockNO);
            }
        }
        //发起调用
        stockDisparityRecordFacade.createOutRecord(putInWarehouseRecordES, outWarehouseRecordES, outOrderNoMap);
    }

}
