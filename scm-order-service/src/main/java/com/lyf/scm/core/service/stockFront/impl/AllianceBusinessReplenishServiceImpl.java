package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.ShopReplenishConstant;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.domain.convert.stockFront.StockRecordDTOConvert;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.*;
import com.lyf.scm.core.remote.item.SkuQtyUnitTool;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.AllianceBusinessReplenishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: AllianceBusinessReplenishServiceImpl
 * <p>
 * @Author: chuwenchao  2020/6/17
 */
@Slf4j
@Service("allianceBusinessReplenishService")
public class AllianceBusinessReplenishServiceImpl implements AllianceBusinessReplenishService {

    @Resource
    private SkuQtyUnitTool skuQtyUnitTool;
    @Resource
    private ReplenishRecordDetailMapper replenishRecordDetailMapper;
    @Resource
    private ReplenishRecordMapper replenishRecordMapper;
    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private StockRecordDTOConvert stockRecordDTOConvert;
    @Resource
    private StockRecordFacade stockRecordFacade;

    /**
     * 确认销售出库
     *
     * @param frontWarehouseRecordRelationE
     */
    @Override
    public void warehouseOutNotify(FrontWarehouseRecordRelationE frontWarehouseRecordRelationE) {

    }

    /**
     * 寻源保存出入库单
     *  @param replenishDetailEList
     * @param realWarehouse
     * @param vmCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAllotWarehouseRecord(List<ReplenishDetailE> replenishDetailEList, RealWarehouse realWarehouse, String vmCode) {
        Integer allotStatus = this.updateAllotDetail(replenishDetailEList);
        String channelCode = replenishDetailEList.get(0).getChannelCode();
        WarehouseRecordE outRecordE = new WarehouseRecordE();
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.DS_REPLENISH_OUT_WAREHOUSE_RECORD.getCode());
        outRecordE.setRecordCode(code);
        outRecordE.setSapOrderCode(replenishDetailEList.get(0).getSapPoNo());
        outRecordE.setRealWarehouseId(realWarehouse.getId());
        outRecordE.setFactoryCode(realWarehouse.getFactoryCode());
        outRecordE.setRealWarehouseCode(realWarehouse.getRealWarehouseOutCode());
        outRecordE.setVirtualWarehouseCode(vmCode);
        outRecordE.setRecordType(WarehouseRecordTypeEnum.DS_REPLENISH_OUT_WAREHOUSE_RECORD.getType());
        outRecordE.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        outRecordE.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
        outRecordE.setChannelCode(channelCode);
        outRecordE.setOutCreateTime(new Date());
        outRecordE.setSyncTmsbStatus(WarehouseRecordConstant.NEED_SYNC_TMSB);
        outRecordE.setSyncDispatchStatus(WarehouseRecordConstant.NEED_DISPATCH);
        //保存后置单
        warehouseRecordMapper.insertWarehouseRecord(outRecordE);
        //基础单位转换
        skuQtyUnitTool.convertRealToBasic(replenishDetailEList);
        //保存明细
        List<Long> frontIds = new ArrayList<>();
        List<WarehouseRecordDetailE> recordDetailEList = new ArrayList<>(replenishDetailEList.size());
        List<FrontWarehouseRecordRelationE> relationEList = new ArrayList<>();
        for(ReplenishDetailE detailE : replenishDetailEList){
            WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
            warehouseRecordDetail.setRecordCode(code);
            warehouseRecordDetail.setWarehouseRecordId(outRecordE.getId());
            warehouseRecordDetail.setSkuId(detailE.getSkuId());
            warehouseRecordDetail.setSkuCode(detailE.getSkuCode());
            //设置为分配数量
            warehouseRecordDetail.setPlanQty(detailE.getBasicSkuQty());
            warehouseRecordDetail.setActualQty(BigDecimal.ZERO);
            warehouseRecordDetail.setUnit(detailE.getBasicUnit());
            warehouseRecordDetail.setUnitCode(detailE.getBasicUnitCode());
            warehouseRecordDetail.setScale(detailE.getScale());
            warehouseRecordDetail.setSapPoNo(detailE.getSapPoNo());
            warehouseRecordDetail.setLineNo(detailE.getLineNo());
            warehouseRecordDetail.setDeliveryLineNo(detailE.getId().toString());
            recordDetailEList.add(warehouseRecordDetail);
            //构造关联关系对象
            if(!frontIds.contains(detailE.getFrontRecordId())) {
                frontIds.add(detailE.getFrontRecordId());
                FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
                relation.setWarehouseRecordId(outRecordE.getId());
                relation.setFrontRecordId(detailE.getFrontRecordId());
                relation.setFrontRecordType(FrontRecordTypeEnum.SHOP_REPLENISHMENT_RECORD.getType());
                relation.setRecordCode(code);
                relation.setFrontRecordCode(detailE.getRecordCode());
                relationEList.add(relation);
            }
        }
        outRecordE.setWarehouseRecordDetailList(recordDetailEList);
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(recordDetailEList);
        //保存关联关系
        for(FrontWarehouseRecordRelationE relation : relationEList) {
            frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
        }
        //更新前置单出库仓库
        for (Long frontId : frontIds) {
            int  i = replenishRecordMapper.updateAllotStatus(frontId, realWarehouse, allotStatus);
            AlikAssert.isTrue(i > 0, ResCode.ORDER_ERROR_5119, ResCode.ORDER_ERROR_5119_DESC);
        }
        //调用库存接口生成出库单
        OutWarehouseRecordDTO outRecord = stockRecordDTOConvert.convertE2OutDTO(outRecordE);
        stockRecordFacade.createOutRecord(outRecord);
    }

    /**
     * @Description: 修改前置单分配明细 <br>
     *
     * @Author chuwenchao 2020/6/17
     * @param replenishDetailEList
     * @return 
     */
    public Integer updateAllotDetail(List<ReplenishDetailE> replenishDetailEList){
        //设置剩余基础数量并设置转化对象属性
        for(ReplenishDetailE detailE: replenishDetailEList){
            detailE.setSkuQty(detailE.getAllotQty());
        }
        skuQtyUnitTool.convertRealToBasic(replenishDetailEList);
        //设置分配的数量
        int allot = 0;
        for(ReplenishDetailE detailE: replenishDetailEList){
            detailE.setLeftBasicSkuQty(detailE.getLeftBasicSkuQty().subtract(detailE.getBasicSkuQty()));
            if(BigDecimal.ZERO.compareTo(detailE.getLeftBasicSkuQty()) == 0){
                allot = allot + 1;
            }
        }
        //更新前置单寻源分配数量及剩余基础单位数量
        replenishRecordDetailMapper.updateAllotDetail(replenishDetailEList);
        if(allot == replenishDetailEList.size()){
            return ShopReplenishConstant.ALLOT_SUCCESS;
        }else {
            //2019-8-15寻源只寻一次
            //return ShopReplenishConstant.NEED_ALLOT;
            return ShopReplenishConstant.ALLOT_SUCCESS;
        }
    }

}
