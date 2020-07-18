package com.lyf.scm.core.service.shopReturn.impl;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.common.enums.shopReturn.ShopReturnAppointTypeEnum;
import com.lyf.scm.common.enums.shopReturn.ShopReturnShopTypeEnum;
import com.lyf.scm.common.enums.shopReturn.ShopReturnTransStatusEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.shopReturn.ShopReturnDTO;
import com.lyf.scm.core.domain.convert.shopReturn.ShopReturnConvert;
import com.lyf.scm.core.domain.convert.shopReturn.ShopReturnDetailConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockInRecordDTOConvert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnDetailE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.shopReturn.ShopReturnDetailMapper;
import com.lyf.scm.core.mapper.shopReturn.ShopReturnMapper;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.item.dto.SkuInfoExtDTO;
import com.lyf.scm.core.remote.item.facade.ItemFacade;
import com.lyf.scm.core.remote.stock.dto.InWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.remote.trade.dto.UpdateReversePoDTO;
import com.lyf.scm.core.remote.trade.dto.UpdateReversePoLineDTO;
import com.lyf.scm.core.remote.trade.facade.TransactionFacade;
import com.lyf.scm.core.service.disparity.DisparityRecordService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.shopReturn.ShopReturnService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */
@Slf4j
@Service("shopReturnService")
public class ShopReturnServiceImpl implements ShopReturnService {

    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private ShopReturnDetailMapper shopReturnDetailMapper;
    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ShopReturnMapper shopReturnMapper;
    @Resource
    private StockInRecordDTOConvert stockInRecordDTOConvert;
    @Resource
    private StockRecordFacade stockRecordFacade;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private ShopReturnConvert shopReturnConvert;
    @Resource
    private ShopReturnDetailConvert shopReturnDetailConvert;
    @Resource
    private ItemFacade itemFacade;
    @Resource
    private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;
    @Resource
    private TransactionFacade transactionFacade;
    @Resource
    private BaseFacade baseFacade;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
    @Resource
    private DisparityRecordService disparityRecordService;

    @Override
    public void warehouseOutNotify(StockNotifyDTO stockNotifyDTO) {
        //查询后置单
        WarehouseRecordE warehouseRecordE = warehouseRecordMapper.queryByRecordCode(stockNotifyDTO.getRecordCode());
        //查询后置单明细
        List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordCode(stockNotifyDTO.getRecordCode());
        warehouseRecordE.setWarehouseRecordDetailList(detailList);
        //Map {前置单明细ID : 后置单明细} 映射
        Map<String, WarehouseRecordDetailE> frontDetailId2RecordDetailMap = detailList.stream()
                .collect(Collectors.toMap(WarehouseRecordDetailE::getDeliveryLineNo, Function.identity()));

        //查询关联关系
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        FrontWarehouseRecordRelationE relationE = relationEList.get(0);

        //通过关系查询前置单--退货单
        ShopReturnE shopReturnE = shopReturnMapper.queryShopReturnById(relationE.getFrontRecordId());
        AlikAssert.isNotNull(shopReturnE, ResCode.ORDER_ERROR_7801, ResCode.ORDER_ERROR_7801_DESC);

        //查询前置单明细 设置实际数量
        List<ShopReturnDetailE> shopReturnDetailEList = shopReturnDetailMapper.selectByRecordCode(shopReturnE.getRecordCode());
        //根据ID更新前置单明细实际数量
        List<ShopReturnDetailE> updateDetail = new ArrayList<>();
        for (ShopReturnDetailE shopReturnDetailE : shopReturnDetailEList) {
            WarehouseRecordDetailE recordDetailE = frontDetailId2RecordDetailMap.get(shopReturnDetailE.getId().toString());
            if (recordDetailE != null) {
                shopReturnDetailE.setBasicSkuQty(recordDetailE.getActualQty());
                updateDetail.add(shopReturnDetailE);
            }
        }
        //更新前置单为已出库(存在多个出库单情况，派车-->已出库)
        if (FrontRecordStatusEnum.TMS.getStatus().equals(shopReturnE.getRecordStatus())) {
            shopReturnMapper.updateToOutAllocation(shopReturnE.getId());
        }

        //设置商品code或id
        itemInfoTool.convertSkuCode(updateDetail);
        //计算前置单明细仓库出库数量
        skuQtyUnitTool.convertBasicToReal(updateDetail);
        //批量更新前置单明细数量
        shopReturnDetailMapper.updateRealRefundQty(updateDetail);

        //构造门店退货-入库单数据
        WarehouseRecordE inRecordE = this.initShopInRecord(shopReturnE, warehouseRecordE);
        //出库单关系 更新入库单 编码
        frontWarehouseRecordRelationMapper.updateDependRecordCodeById(inRecordE.getRecordCode(), relationE.getId());
        //同步库存中心入库单据
        InWarehouseRecordDTO inRecordDTO = stockInRecordDTOConvert.convertE2InDTO(inRecordE);
        stockRecordFacade.createInRecord(inRecordDTO);
    }

    @Override
    public void warehouseInNotify(StockNotifyDTO stockNotifyDTO) {
        //查询后置单明细
        List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordCode(stockNotifyDTO.getRecordCode());
        //Map {前置单明细ID : 后置单明细} 映射
        Map<String, WarehouseRecordDetailE> frontDetailId2RecordDetailMap = detailList.stream()
                .collect(Collectors.toMap(WarehouseRecordDetailE::getDeliveryLineNo, Function.identity()));

        //查询关联关系
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(stockNotifyDTO.getRecordCode());
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        FrontWarehouseRecordRelationE relationE = relationEList.get(0);

        //通过关系查询前置单--退货单
        ShopReturnE shopReturnE = shopReturnMapper.queryShopReturnById(relationE.getFrontRecordId());
        AlikAssert.isNotNull(shopReturnE, ResCode.ORDER_ERROR_7801, ResCode.ORDER_ERROR_7801_DESC);

        //查询前置单明细 设置实际数量
        List<ShopReturnDetailE> shopReturnDetailEList = shopReturnDetailMapper.selectByRecordCode(shopReturnE.getRecordCode());
        List<ShopReturnDetailE> updateDetail = new ArrayList<>();
        for (ShopReturnDetailE shopReturnDetailE : shopReturnDetailEList) {
            WarehouseRecordDetailE recordDetailE = frontDetailId2RecordDetailMap.get(shopReturnDetailE.getId().toString());
            if (recordDetailE != null) {
                shopReturnDetailE.setBasicSkuQty(recordDetailE.getActualQty());
                updateDetail.add(shopReturnDetailE);
            }
        }

        //更新前置单为已入库
        if (FrontRecordStatusEnum.OUT_ALLOCATION.getStatus().equals(shopReturnE.getRecordStatus())) {
            shopReturnMapper.updateToInAllocation(shopReturnE.getId());
        }
        //前置单下的所有出库单和入库单都已经回调后 更新待同步交易状态
        List<Long> whRecordId = frontWarehouseRecordRelationMapper.queryWarehouseRecordIdByRecord(shopReturnE.getRecordCode());
        if (CollectionUtils.isNotEmpty(whRecordId)) {
            List<WarehouseRecordE> whRecordList = warehouseRecordMapper.queryWarehouseRecordByIds(whRecordId);
            boolean allOutAndIn = true;
            for (WarehouseRecordE record : whRecordList) {
                if (WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType().equals(record.getBusinessType()) &&
                        !WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus().equals(record.getRecordStatus())) {
                    allOutAndIn = false;
                    break;
                }
                if (WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType().equals(record.getBusinessType()) &&
                        !WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus().equals(record.getRecordStatus())) {
                    allOutAndIn = false;
                    break;
                }
            }
            if (allOutAndIn) {
                //更新前置单 推送交易状态-->待推送
                shopReturnMapper.updateRecordTransStatusToUnPush(shopReturnE.getId());
            }
        }

        //设置商品code或id
        itemInfoTool.convertSkuCode(updateDetail);
        //更新前置单明细仓库出库数量
        skuQtyUnitTool.convertBasicToReal(updateDetail);
        //批量更新明细数量
        shopReturnDetailMapper.updateRealEnterQty(updateDetail);
        //计算差异订单数据-Lin.Xu 2020年7月18日9:34:02
        disparityRecordService.addDisparityRecord(stockNotifyDTO.getRecordCode(), relationEList);
    }

    /**
     * 根据出库单--创建入库单(门店退货)
     */
    private WarehouseRecordE initShopInRecord(ShopReturnE shopReturnE, WarehouseRecordE warehouseRecordE) {
        WarehouseRecordE inRecordE = new WarehouseRecordE();
        //根据出库单类型 确定 入库单类型
        Integer outRecordType = warehouseRecordE.getRecordType();
        if (WarehouseRecordTypeEnum.DS_RETURN_OUT_WAREHOUSE_RECORD.getType().equals(outRecordType)) {
            //直营门店退货出库单 --> 直营门店退货入库单
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.DS_RETURN_IN_WAREHOUSE_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.DS_RETURN_IN_WAREHOUSE_RECORD.getType());
            //设置仓库信息
            inRecordE.setRealWarehouseId(shopReturnE.getInRealWarehouseId());
            inRecordE.setFactoryCode(shopReturnE.getInFactoryCode());
            inRecordE.setRealWarehouseCode(shopReturnE.getInRealWarehouseCode());
        } else if (WarehouseRecordTypeEnum.LS_RETURN_OUT_WAREHOUSE_RECORD.getType().equals(outRecordType)) {
            //加盟门店退货出库单 --> 加盟门店退货入库单
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.LS_RETURN_IN_WAREHOUSE_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.LS_RETURN_IN_WAREHOUSE_RECORD.getType());
            //设置仓库信息
            inRecordE.setRealWarehouseId(shopReturnE.getInRealWarehouseId());
            inRecordE.setFactoryCode(shopReturnE.getInFactoryCode());
            inRecordE.setRealWarehouseCode(shopReturnE.getInRealWarehouseCode());
        } else if (WarehouseRecordTypeEnum.DS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getType().equals(outRecordType)) {
            //直营门店冷链退货出库单 --> 直营门店冷链退货入库单
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.DS_RETURN_COLD_IN_WAREHOUSE_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.DS_RETURN_COLD_IN_WAREHOUSE_RECORD.getType());
            //设置仓库信息
            inRecordE.setRealWarehouseId(shopReturnE.getInColdRealWarehouseId());
            inRecordE.setFactoryCode(shopReturnE.getInColdFactoryCode());
            inRecordE.setRealWarehouseCode(shopReturnE.getInColdRealWarehouseCode());
        } else if (WarehouseRecordTypeEnum.LS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getType().equals(outRecordType)) {
            //加盟门店冷链退货出库单 --> 直营门店冷链退货入库单
            String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.LS_RETURN_COLD_IN_WAREHOUSE_RECORD.getCode());
            inRecordE.setRecordCode(code);
            inRecordE.setRecordType(WarehouseRecordTypeEnum.LS_RETURN_COLD_IN_WAREHOUSE_RECORD.getType());
            //设置仓库信息
            inRecordE.setRealWarehouseId(shopReturnE.getInColdRealWarehouseId());
            inRecordE.setFactoryCode(shopReturnE.getInColdFactoryCode());
            inRecordE.setRealWarehouseCode(shopReturnE.getInColdRealWarehouseCode());
        }
        inRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        inRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
        inRecordE.setChannelCode(shopReturnE.getChannelCode());
        inRecordE.setOutCreateTime(shopReturnE.getOutCreateTime());
        inRecordE.setSapOrderCode(warehouseRecordE.getSapOrderCode());
        warehouseRecordMapper.insertWarehouseRecord(inRecordE);

        List<WarehouseRecordDetailE> detailList = warehouseRecordE.getWarehouseRecordDetailList();
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>(detailList.size());
        //保存明细
        for (WarehouseRecordDetailE detailE : detailList) {
            WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
            BeanUtils.copyProperties(detailE, warehouseRecordDetail);
            warehouseRecordDetail.setRecordCode(inRecordE.getRecordCode());
            warehouseRecordDetail.setWarehouseRecordId(inRecordE.getId());
            warehouseRecordDetail.setPlanQty(detailE.getActualQty());
            warehouseRecordDetail.setActualQty(BigDecimal.ZERO);
            warehouseRecordDetail.setSapPoNo(shopReturnE.getSapPoNo());
            warehouseRecordDetailList.add(warehouseRecordDetail);
        }
        inRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordDetailList);

        //保存关联关系
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(inRecordE.getId());
        relation.setFrontRecordId(shopReturnE.getId());
        relation.setFrontRecordType(shopReturnE.getRecordType());
        relation.setRecordCode(inRecordE.getRecordCode());
        relation.setFrontRecordCode(shopReturnE.getRecordCode());
        //入库单关系 记录出库单 编码
        relation.setDependRecordCode(warehouseRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
        return inRecordE;
    }

    /**
     * @param outRecordCode
     * @description: 门店退货取消
     * @author: zys
     * @time: 2020/7/15 9:41
     */
    @Override
    public Integer shopReturnCancel(String outRecordCode) {
        //幂等校验
       // TODO 确定返回结果
        ShopReturnE shopReturnE = shopReturnMapper.selectByOutRecordCode(outRecordCode);
        if (shopReturnE == null) {
            log.info("退货单不存在");
            throw new RomeException(ResCode.ORDER_ERROR_7801, ResCode.ORDER_ERROR_7801_DESC);
        }
        // 取消结果 0 不能取消 1 已取消
       Integer cancelResult = 0;
       //获取单据状态
        Integer recordStatus = shopReturnE.getRecordStatus();
        if (recordStatus == 0) {

            //根据门店前置单号查询出出入库单号
            List<String> warehouseRecodeList = frontWarehouseRecordRelationMapper.getRecordCodeByFrontRecordCode(shopReturnE.getRecordCode());
            //根据单据编号查询出入库单据列表
            List<WarehouseRecordE> warehouseRecordElist = warehouseRecordMapper.queryWarehouseRecordByRecordCode(warehouseRecodeList);
            //筛选出出库单
            List<WarehouseRecordE> warehouseRecordEs = warehouseRecordElist.stream().filter(whRecord -> WarehouseRecordBusinessTypeEnum.
                        OUT_WAREHOUSE_RECORD.getType().equals(whRecord.getBusinessType())).collect(Collectors.toList());
            //遍历出库单
            int num = 0;
            for (WarehouseRecordE whRecord : warehouseRecordEs) {
                //判断出库单同步派车系统状态
                if (SyncTmsBStatusEnum.SYNCHRONIZED.getStatus().equals(whRecord.getSyncTmsbStatus())) {
                    num++;
                }
            }
            //只要有一个出库单的同步派车状态为已派车 则不能取消
            if (num > 0) {
                return cancelResult;
            }
            //可以取消
            //修改前置单据状态为已取消
            shopReturnMapper.updateRecordStatus(shopReturnE.getRecordCode());
            //批量修改出库单同步派车系统状态为无需派车 单据状态为取消订单
            warehouseRecordMapper.updateRecordAndSyncTmsbStatus(warehouseRecordElist);
            cancelResult = 1;
            return cancelResult;

        } else {
            return cancelResult;
        }
    }

    /**
     * @param recordCode
     * @description: 门店退货派车回调
     * @author: zys
     * @time: 2020/7/15 17:15
     */
    @Override
    public void dispatchResultShopReturnComplete(String recordCode) {
        //获取后置单与前置单的关联关系对象
        List<FrontWarehouseRecordRelationE> relationEList = frontWarehouseRecordRelationMapper.getFrontRelationByRecordCode(recordCode);
        AlikAssert.isNotEmpty(relationEList, ResCode.ORDER_ERROR_5120, ResCode.ORDER_ERROR_5120_DESC);
        List<Long> frontRecordIdList = relationEList.stream().map(FrontWarehouseRecordRelationE::getFrontRecordId).distinct().collect(Collectors.toList());
        //修改前置单据状态 同步派车系统状态
        shopReturnMapper.updateIsNeedDispatchComplete(frontRecordIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShopReturn(ShopReturnDTO shopReturnDTO) {
        //1.幂等校验
        ShopReturnE shopReturnE = shopReturnMapper.selectByOutRecordCode(shopReturnDTO.getOutRecordCode());
        if (null != shopReturnE) {
            return;
        }
        //通过门店编码获取门店发货仓
        RealWarehouse shopRealWarehouse = stockRealWarehouseFacade.queryRealWarehouseByShopCode(shopReturnDTO.getShopCode());
        if (null == shopRealWarehouse) {
            throw new RomeException(ResCode.ORDER_ERROR_7803, ResCode.ORDER_ERROR_7803_DESC);
        }
        StoreDTO storeDTO = baseFacade.searchByCode(shopReturnDTO.getShopCode());
        if (null == storeDTO) {
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
        RealWarehouse onlineRealWarehous;
        RealWarehouse coludRealWarehouse = null;
        shopReturnE = shopReturnConvert.convertDTO2E(shopReturnDTO);
        List<String> skuCodeList = new ArrayList<String>();
        //判断是否指定仓
        if (shopReturnDTO.getIsAppoint().equals(ShopReturnAppointTypeEnum.IS_NOT_APPOINT.getType())) {
            //获取线下退货仓
            List<RealWarehouse> onlineRealWarehousList = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndRealWarehouseType(storeDTO.getDeliveryFactory(), RealWarehouseTypeEnum.RW_TYPE_12.getType());
            if (CollectionUtils.isNotEmpty(onlineRealWarehousList)) {
                onlineRealWarehous = onlineRealWarehousList.get(0);
            } else {
                throw new RomeException(ResCode.ORDER_ERROR_7802, ResCode.ORDER_ERROR_7802_DESC);
            }
            //获取冷链退货仓
            List<RealWarehouse> coludRealWarehouseList = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndRealWarehouseType(storeDTO.getDeliveryFactory(), RealWarehouseTypeEnum.RW_TYPE_23.getType());
            if (CollectionUtils.isNotEmpty(coludRealWarehouseList)) {
                coludRealWarehouse = coludRealWarehouseList.get(0);
                shopReturnE.setInColdFactoryCode(coludRealWarehouse.getFactoryCode());
                shopReturnE.setInColdRealWarehouseId(coludRealWarehouse.getId());
                shopReturnE.setInColdRealWarehouseCode(coludRealWarehouse.getRealWarehouseOutCode());
            }
        } else {
            //校验指定仓
            onlineRealWarehous = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(shopReturnDTO.getInRealWarehouseCode(), shopReturnDTO.getInFactoryCode());
            if (null == onlineRealWarehous) {
                throw new RomeException(ResCode.ORDER_ERROR_7802, ResCode.ORDER_ERROR_7802_DESC);
            }
        }
        String recordCode;
        //根据不同的前置单类型生成编码
        if (shopReturnDTO.getShopType().equals(ShopReturnShopTypeEnum.DIRECT_SALE.getType())) {
            recordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getCode());
            shopReturnE.setRecordType(FrontRecordTypeEnum.DIRECT_SHOP_RETURN_GOODS_RECORD.getType());
        } else {
            recordCode = orderUtilService.queryOrderCode(FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getCode());
            shopReturnE.setRecordType(FrontRecordTypeEnum.JOIN_SHOP_RETURN_GOODS_RECORD.getType());
        }
        shopReturnE.setRecordCode(recordCode);
        shopReturnE.setInFactoryCode(onlineRealWarehous.getFactoryCode());
        shopReturnE.setInRealWarehouseCode(onlineRealWarehous.getRealWarehouseOutCode());
        shopReturnE.setInRealWarehouseId(onlineRealWarehous.getId());
        shopReturnE.setOutFactoryCode(shopRealWarehouse.getFactoryCode());
        shopReturnE.setOutRealWarehouseCode(shopRealWarehouse.getRealWarehouseOutCode());
        shopReturnE.setOutRealWarehouseId(shopRealWarehouse.getId());
        shopReturnE.setSapPoNo(shopReturnDTO.getSapReverseNo());
        shopReturnE.setRecordStatus(FrontRecordStatusEnum.INIT.getStatus());
        shopReturnE.setIsNeedDispatch(1);
        List<ShopReturnDetailE> shopReturnDetailEList = shopReturnDetailConvert.convertDTOList2EList(shopReturnDTO.getFrontRecordDetails());
        //获取到所有的skuCode
        shopReturnDetailEList.stream().forEach(shopReturnDetailE -> {
            shopReturnDetailE.setRecordCode(recordCode);
            skuCodeList.add(shopReturnDetailE.getSkuCode());
        });
        itemInfoTool.convertSkuCode(shopReturnDetailEList);
        skuQtyUnitTool.convertRealToBasic(shopReturnDetailEList);
        //插入门店退货单
        if (shopReturnMapper.insert(shopReturnE) < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
        //插入门店退货单明细
        if (shopReturnDetailMapper.batchInsertDetail(shopReturnDetailEList) < 1) {
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
        //冷链list
        List<ShopReturnDetailE> coldShopReturnDetailE = new ArrayList<ShopReturnDetailE>();
        //存在冷链仓进行分组
        if (null != coludRealWarehouse) {
            skuCodeList.stream().distinct().collect(Collectors.toList());
            List<SkuInfoExtDTO> skuInfoExtDTOList = itemFacade.skuBySkuCodes(skuCodeList);
            shopReturnDetailEList.stream().forEach(shopReturnDetailE -> {
                skuInfoExtDTOList.stream().filter((skuInfoExtDTO -> skuInfoExtDTO.getSkuCode().equals(shopReturnDetailE.getSkuCode()))).forEach(skuInfoExtDTO -> {
                    if (skuInfoExtDTO.getLoadingGroup().equals("0002")) {
                        coldShopReturnDetailE.add(shopReturnDetailE);
                    }
                });
            });
            shopReturnDetailEList.removeAll(coldShopReturnDetailE);
        }
        //判断是否是直营门店
        String warehouseRecordCode;
        WarehouseRecordE coldWarehouseRecord = null;
        WarehouseRecordE ordinaryWarehouseRecord = null;
        if (shopReturnDTO.getShopType().equals(ShopReturnShopTypeEnum.DIRECT_SALE.getType())) {
            //直营门店普通退货出库生成
            if (CollectionUtils.isNotEmpty(shopReturnDetailEList)) {
                warehouseRecordCode = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.DS_RETURN_OUT_WAREHOUSE_RECORD.getCode());
                ordinaryWarehouseRecord = createWarehouseRecord(shopReturnE, shopRealWarehouse, WarehouseRecordTypeEnum.DS_RETURN_OUT_WAREHOUSE_RECORD, shopReturnDetailEList, warehouseRecordCode);
            }
            //直营门店冷链退货出库调拨生成
            if (CollectionUtils.isNotEmpty(coldShopReturnDetailE)) {
                warehouseRecordCode = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.DS_RETURN_COLD_OUT_WAREHOUSE_RECORD.getCode());
                coldWarehouseRecord = createWarehouseRecord(shopReturnE, shopRealWarehouse, WarehouseRecordTypeEnum.DS_RETURN_COLD_OUT_WAREHOUSE_RECORD, coldShopReturnDetailE, warehouseRecordCode);
            }
        } else {
            //加盟门店普通退货出库调拨生成
            if (CollectionUtils.isNotEmpty(shopReturnDetailEList)) {
                warehouseRecordCode = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.LS_RETURN_OUT_WAREHOUSE_RECORD.getCode());
                ordinaryWarehouseRecord = createWarehouseRecord(shopReturnE, shopRealWarehouse, WarehouseRecordTypeEnum.LS_RETURN_OUT_WAREHOUSE_RECORD, shopReturnDetailEList, warehouseRecordCode);
            }
            //加盟门店冷链退货出库调拨生成
            if (CollectionUtils.isNotEmpty(coldShopReturnDetailE)) {
                warehouseRecordCode = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.LS_RETURN_COLD_IN_WAREHOUSE_RECORD.getCode());
                coldWarehouseRecord = createWarehouseRecord(shopReturnE, shopRealWarehouse, WarehouseRecordTypeEnum.LS_RETURN_COLD_IN_WAREHOUSE_RECORD, coldShopReturnDetailE, warehouseRecordCode);
            }
        }
        //同步出库单 到 库存中心
        try {
            if (null != ordinaryWarehouseRecord) {
                OutWarehouseRecordDTO outWarehouseRecordDTO = stockRecordDTOConvert.convertE2OutDTO(ordinaryWarehouseRecord);
                outWarehouseRecordDTO.setWarehouseCode(ordinaryWarehouseRecord.getRealWarehouseCode());
                stockRecordFacade.createOutRecord(outWarehouseRecordDTO);
            }
            if (null != coldWarehouseRecord) {
                OutWarehouseRecordDTO outWarehouseRecordDTO = stockRecordDTOConvert.convertE2OutDTO(ordinaryWarehouseRecord);
                outWarehouseRecordDTO.setWarehouseCode(coldWarehouseRecord.getRealWarehouseCode());
                stockRecordFacade.createOutRecord(outWarehouseRecordDTO);
            }
        } catch (
                RomeException e) {
            log.info(e.getMessage(), e);
            throw new RomeException(ResCode.ORDER_ERROR_7306, ResCode.ORDER_ERROR_7306_DESC);
        } catch (
                Exception e) {
            log.info(e.getMessage(), e);
            throw new RomeException(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }

    }

    private WarehouseRecordE createWarehouseRecord(ShopReturnE shopReturnE, RealWarehouse realWarehouse, WarehouseRecordTypeEnum warehouseRecordTypeEnum, List<ShopReturnDetailE> shopReturnDetailEList, String warehouseRecordCode) {
        WarehouseRecordE warehouseRecordE = new WarehouseRecordE();
        AlikAssert.isNotNull(realWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
        warehouseRecordE.setRealWarehouseId(realWarehouse.getId());
        warehouseRecordE.setRecordCode(warehouseRecordCode);
        warehouseRecordE.setSapOrderCode(shopReturnE.getSapPoNo());
        warehouseRecordE.setFactoryCode(realWarehouse.getFactoryCode());
        warehouseRecordE.setRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        warehouseRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecordE.setRecordType(warehouseRecordTypeEnum.getType());
        warehouseRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
        warehouseRecordE.setOutCreateTime(shopReturnE.getOutCreateTime());
        warehouseRecordE.setSyncWmsStatus(WmsSyncStatusEnum.NO_REQUIRED.getStatus());
        warehouseRecordE.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        warehouseRecordE.setSyncTmsbStatus(SyncTmsBStatusEnum.UNSYNCHRONIZED.getStatus());
        shopReturnE.setShopReturnDetailEList(shopReturnDetailEList);
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(shopReturnDetailEList)) {
            shopReturnDetailEList.forEach(shopReturnDetailE -> {
                WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
                warehouseRecordDetailE.setSkuCode(shopReturnDetailE.getSkuCode());
                warehouseRecordDetailE.setSapPoNo(shopReturnE.getSapPoNo());
                //此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
                warehouseRecordDetailE.setPlanQty(shopReturnDetailE.getBasicSkuQty());
                warehouseRecordDetailE.setUnit(shopReturnDetailE.getBasicUnit());
                warehouseRecordDetailE.setUnitCode(shopReturnDetailE.getBasicUnitCode());
                warehouseRecordDetailE.setActualQty(BigDecimal.ZERO);
                warehouseRecordDetailE.setRealWarehouseId(realWarehouse.getId());
                warehouseRecordDetailE.setLineNo(shopReturnDetailE.getLineNo());
                warehouseRecordDetailE.setDeliveryLineNo(String.valueOf(shopReturnDetailE.getId()));
                warehouseRecordDetailList.add(warehouseRecordDetailE);
            });
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecordE.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }
        warehouseRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        //创建后置单
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecordE);
        warehouseRecordE.getWarehouseRecordDetailList().forEach(detailE -> {
            detailE.setWarehouseRecordId(warehouseRecordE.getId());
            detailE.setRecordCode(warehouseRecordE.getRecordCode());
        });
        //保存后置单明细
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecordE.getWarehouseRecordDetailList());
        //保存前置单 + 后置单关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecordE, shopReturnE);
        return warehouseRecordE;
    }

    @Override
    public List<String> queryUnPushTradeShopReturnRecord(Integer page, Integer maxResult) {
        return shopReturnMapper.queryUnPushTradeShopReturnRecord(page, maxResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlePushTradeShopReturnRecord(String frontRecordCode) {
        if (StringUtils.isEmpty(frontRecordCode)) {
            log.info("前置单据号为空");
            return;
        }
        ShopReturnE shopReturn = shopReturnMapper.selectByRecordCode(frontRecordCode);
        if (null == shopReturn || !FrontRecordStatusEnum.IN_ALLOCATION.getStatus().equals(shopReturn.getRecordStatus()) ||
                !ShopReturnTransStatusEnum.UN_PUSH.getStatus().equals(shopReturn.getTransStatus())) {
            log.info("门店退货单为空或未入库，无需同步，前置单号：{}", frontRecordCode);
            return;
        }
        List<ShopReturnDetailE> returnDetailList = shopReturnDetailMapper.selectByRecordCode(frontRecordCode);
        if (CollectionUtils.isEmpty(returnDetailList)) {
            log.info("门店退货单明细为空，前置单号：{}", frontRecordCode);
            return;
        }
        log.info("门店退货-推交易，前置单号：{}", frontRecordCode);
        UpdateReversePoDTO updatePo = new UpdateReversePoDTO();
        updatePo.setDoNo(shopReturn.getOutRecordCode());
        List<UpdateReversePoLineDTO> poLineList = new ArrayList<>();
        returnDetailList.forEach(shopDetail -> {
            UpdateReversePoLineDTO poLine = new UpdateReversePoLineDTO();
            poLine.setReversePoNo(shopReturn.getOutRecordCode());
            poLine.setLineNo(shopDetail.getLineNo());
            poLine.setActualDeliveryQuantity(shopDetail.getRealRefundQty());
            poLine.setActualReceiveQuantity(shopDetail.getRealEnterQty());
            poLine.setPredictDeliveryQuantity(shopDetail.getSkuQty());
            poLine.setSaleUnitCode(shopDetail.getUnitCode());
            poLine.setSkuCode(shopDetail.getSkuCode());
            poLine.setSkuId(shopDetail.getSkuId());
            poLineList.add(poLine);
        });
        updatePo.setReversePoLineQuantityDTOs(poLineList);

        //更新推交易状态
        int result = shopReturnMapper.updateShopReturnTransStatusPushed(shopReturn.getId());
        AlikAssert.isTrue(result > 0, ResCode.ORDER_ERROR_7805, ResCode.ORDER_ERROR_7805_DESC);
        //推交易
        log.info("门店退货-推送交易参数：{}，前置单号：{}", JSON.toJSONString(updatePo), frontRecordCode);
        transactionFacade.shopReturnPushTransactionReverseStatus(updatePo);
        log.info("门店退货-推交易成功，前置单号：{}", frontRecordCode);
    }

}
