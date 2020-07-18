package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.entity.stockFront.*;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import com.lyf.scm.core.service.stockFront.ShopAllocationToWareHouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zys
 * @Description
 * @date 2020/6/16 13:59
 * @Version
 */
@Slf4j
@Service("adjustAllocationToWareHouseRecordService")
public class ShopAllocationToWareHouseRecordServiceImpl implements ShopAllocationToWareHouseRecordService {

    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private OrderUtilService orderUtilService;
    @Autowired
    private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;

    /**
     * 根据调拨单生成出库单
     *
     * @param frontRecordE
     * @return
     */
    @Override
    public WarehouseRecordE createOutRecordByFrontRecord(ShopAllocationE frontRecordE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        //根据前置单生成出库单数据
        String code =orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_ALLOCATION_OUT_WAREHOUSE_RECORD.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(frontRecordE.getOutRealWarehouseId());
        warehouseRecord.setFactoryCode(frontRecordE.getOutFactoryCode());
        warehouseRecord.setRealWarehouseCode(frontRecordE.getOutRealWarehouseCode());
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.SHOP_ALLOCATION_OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setCmpStatus(WarehouseRecordConstant.INIT_CMP);
        warehouseRecord.setOutCreateTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setDeliveryTime(frontRecordE.getOutCreateTime());
        List<ShopAllocationDetailE> frontRecordDetails = frontRecordE.getFrontRecordDetails();

        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
        WarehouseRecordDetailE warehouseRecordDetail = null;
        if (frontRecordDetails != null) {
            for (ShopAllocationDetailE detailE : frontRecordDetails) {
                //前置单明细生成后置单明细
                warehouseRecordDetail = createRecordDetailByActualFrontRecord(detailE);
                warehouseRecordDetail.setRealWarehouseId(warehouseRecord.getRealWarehouseId());
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
        }
        warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);

        //创建门店调拨出库单
        saveWareHouseRecord(warehouseRecord, frontRecordE);

        return warehouseRecord;
    }

    /**
     * 根据门店调拨单 创建入库单
     *
     * @param frontRecordE
     */
    @Override
    public WarehouseRecordE createInRecordByFrontRecord(ShopAllocationE frontRecordE) {

        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        //根据前置单生成入库单数据
        String code =orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_ALLOCATION_IN_WAREHOUSE_RECORD.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(frontRecordE.getInRealWarehouseId());
        warehouseRecord.setFactoryCode(frontRecordE.getInFactoryCode());
        warehouseRecord.setRealWarehouseCode(frontRecordE.getInRealWarehouseCode());
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.SHOP_ALLOCATION_IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setReceiverTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setOutCreateTime(frontRecordE.getOutCreateTime());
        if (frontRecordE.getBusinessType() == 1) {
            warehouseRecord.setCmpStatus(WarehouseRecordConstant.NEED_CMP);
        } else {
            warehouseRecord.setCmpStatus(WarehouseRecordConstant.INIT_CMP);
        }
        warehouseRecord.setSyncWmsStatus(WmsSyncStatusEnum.NO_REQUIRED.getStatus());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.NEED_TRANSFER);

        List<ShopAllocationDetailE> frontRecordDetails = frontRecordE.getFrontRecordDetails();
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
        WarehouseRecordDetailE warehouseRecordDetail = null;
        if (frontRecordDetails != null) {
            for (ShopAllocationDetailE detailE : frontRecordDetails) {
                //前置单明细生成后置单明细
                warehouseRecordDetail = createRecordDetailByActualFrontRecord(detailE);
                warehouseRecordDetail.setRealWarehouseId(warehouseRecord.getRealWarehouseId());
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
        }

        warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);

        //创建门店调拨入库单
        saveWareHouseRecord(warehouseRecord, frontRecordE);

        return warehouseRecord;
    }

    /**
     * 根据前置单生成单据明细(实际出库数就是计划数)
     */
    private WarehouseRecordDetailE createRecordDetailByActualFrontRecord(ShopAllocationDetailE detailE) {
        WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
        warehouseRecordDetail.setSkuId(detailE.getSkuId());
        warehouseRecordDetail.setDeliveryLineNo(detailE.getId().toString());
        warehouseRecordDetail.setSkuCode(detailE.getSkuCode());
        warehouseRecordDetail.setPlanQty(detailE.getBasicSkuQty());
        warehouseRecordDetail.setActualQty(detailE.getBasicSkuQty());
        warehouseRecordDetail.setUnit(detailE.getBasicUnit());
        warehouseRecordDetail.setUnitCode(detailE.getBasicUnitCode());
        if (null != detailE.getBatchStocks()) {
            List<WarehouseBatchStockE> batchStocks = new ArrayList<>(detailE.getBatchStocks().size());
            for (FrontBatchStockE frontBatchStockE : detailE.getBatchStocks()) {
                createBatchStock(frontBatchStockE, detailE, batchStocks);
            }
        }

        return warehouseRecordDetail;
    }

    /**
     * 生成批次
     *
     * @param frontBatchStockE
     */
    private void createBatchStock(FrontBatchStockE frontBatchStockE, ShopAllocationDetailE detailE, List<WarehouseBatchStockE> batchStocks) {
        WarehouseBatchStockE batchStockE = new WarehouseBatchStockE();
        batchStockE.setBatchCode(frontBatchStockE.getBatchCode());
        batchStockE.setSkuId(detailE.getSkuId());
        batchStockE.setSkuCode(detailE.getSkuCode());
        batchStockE.setSkuQty(frontBatchStockE.getBasicSkuQty());
        batchStockE.setUnit(frontBatchStockE.getBasicUnit());
        batchStockE.setUnitCode(frontBatchStockE.getBasicUnitCode());
        batchStocks.add(batchStockE);
    }

    /**
     * 保存后置单 主表
     *
     * @param warehouseRecord
     * @param detailE
     * @return
     */
    public boolean saveWareHouseRecord(WarehouseRecordE warehouseRecord, ShopAllocationE detailE) {
        //保存后置单主表信息
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecord);

        //保存后置单 明细表 数据
        warehouseRecord.getWarehouseRecordDetailList().forEach(record -> {
            record.setWarehouseRecordId(warehouseRecord.getId());
            record.setRecordCode(warehouseRecord.getRecordCode());
            record.setRealWarehouseId(warehouseRecord.getRealWarehouseId());
            record.setChannelCode(warehouseRecord.getChannelCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecord.getWarehouseRecordDetailList());

        //保存前置单和出库单一对一关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecord, detailE);
        return Boolean.TRUE;
    }
}
