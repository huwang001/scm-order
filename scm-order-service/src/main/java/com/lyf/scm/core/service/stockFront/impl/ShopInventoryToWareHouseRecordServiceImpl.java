package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import com.lyf.scm.core.service.stockFront.ShopInventoryToWareHouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanlong
 */
@Slf4j
@Service("shopInventoryToWareHouseRecordService")
public class ShopInventoryToWareHouseRecordServiceImpl implements ShopInventoryToWareHouseRecordService {

    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;
    @Resource
    private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;
    @Resource
    private OrderUtilService orderUtilService;

    @Override
    public WarehouseRecordE createOutWarehouseRecordByShopInventory(ShopInventoryE frontRecordE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_INVENTORY_OUT_WAREHOUSE_RECORD.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(frontRecordE.getRealWarehouseId());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.SHOP_INVENTORY_OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setDeliveryTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setMerchantId(frontRecordE.getMerchantId());
        warehouseRecord.setOutCreateTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //状态为已出库状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
        warehouseRecord.setFactoryCode(frontRecordE.getFactoryCode());
        warehouseRecord.setRealWarehouseCode(frontRecordE.getRealWarehouseCode());

        List<ShopInventoryDetailE> frontRecordDetails = frontRecordE.getFrontRecordDetails();
        if (frontRecordDetails != null) {
            List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
            for (ShopInventoryDetailE detailE : frontRecordDetails) {
                WarehouseRecordDetailE warehouseRecordDetail = createRecordDetailByActualFrontRecord(detailE);
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }
        //保存出库单 详情 关系
        addWarehouseRecord(warehouseRecord, frontRecordE);
        return warehouseRecord;
    }

    @Override
    public WarehouseRecordE createInWarehouseRecordByShopInventory(ShopInventoryE frontRecordE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_INVENTORY_IN_WAREHOUSE_RECORD.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(frontRecordE.getRealWarehouseId());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.SHOP_INVENTORY_IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setMerchantId(frontRecordE.getMerchantId());
        warehouseRecord.setOutCreateTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setReceiverTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //状态为已入库状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus());
        warehouseRecord.setFactoryCode(frontRecordE.getFactoryCode());
        warehouseRecord.setRealWarehouseCode(frontRecordE.getRealWarehouseCode());

        List<ShopInventoryDetailE> frontRecordDetails = frontRecordE.getFrontRecordDetails();
        if (frontRecordDetails != null) {
            List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
            for (ShopInventoryDetailE detailE : frontRecordDetails) {
                WarehouseRecordDetailE warehouseRecordDetail = createRecordDetailByActualFrontRecord(detailE);
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }
        //保存入库单 详情 关系
        addWarehouseRecord(warehouseRecord, frontRecordE);
        return warehouseRecord;
    }

    /**
     * 前置单明细 --> 创建后置单明细
     *
     * @param frontRecordDetail
     * @return
     */
    public WarehouseRecordDetailE createRecordDetailByActualFrontRecord(ShopInventoryDetailE frontRecordDetail) {
        WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
        warehouseRecordDetail.setSkuId(frontRecordDetail.getSkuId());
        warehouseRecordDetail.setSkuCode(frontRecordDetail.getSkuCode());
        warehouseRecordDetail.setPlanQty(frontRecordDetail.getBasicSkuQty());
        warehouseRecordDetail.setActualQty(frontRecordDetail.getBasicSkuQty());
        warehouseRecordDetail.setUnit(frontRecordDetail.getBasicUnit());
        warehouseRecordDetail.setUnitCode(frontRecordDetail.getBasicUnitCode());
        //设置后置单明细交货行号 = 前置单明细Id 由于盘点存在明细不完全一致的情况 这里前置单明细存在就保存
        if(null != frontRecordDetail.getId()) {
            warehouseRecordDetail.setDeliveryLineNo(String.valueOf(frontRecordDetail.getId()));
        }
        return warehouseRecordDetail;
    }

    /**
     * 保存出库单(入库单) 明细 关系
     *
     * @param warehouseRecord
     * @param frontRecordE
     */
    private void addWarehouseRecord(WarehouseRecordE warehouseRecord, ShopInventoryE frontRecordE) {
        //保存出库单
        if (warehouseRecord.getSyncTransferStatus() == null) {
            warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
        }
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecord);

        //保存出库单明细
        List<WarehouseRecordDetailE> recordDetailEList = warehouseRecord.getWarehouseRecordDetailList();
        recordDetailEList.forEach(record -> {
            record.setWarehouseRecordId(warehouseRecord.getId());
            record.setRecordCode(warehouseRecord.getRecordCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(recordDetailEList);

        //保存前置单 + 后置单关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecord, frontRecordE);
    }

    @Override
    public List<WarehouseRecordE> queryInventoryWarehouseById(Long frontRecordId) {
        List<Long> ids = frontWarehouseRecordRelationMapper.queryWarehouseIdByFrontId(frontRecordId, FrontRecordTypeEnum.SHOP_INVENTORY_RECORD.getType());
        if (ids != null && ids.size() > 0) {
            List<WarehouseRecordE> list = warehouseRecordMapper.queryWarehouseRecordByIds(ids);
            List<WarehouseRecordDetailE> detailList = warehouseRecordDetailMapper.queryListByRecordIds(ids);
            list.forEach(record -> {
                List<WarehouseRecordDetailE> details = detailList.stream().filter(detail ->
                        detail.getWarehouseRecordId().equals(record.getId())).collect(Collectors.toList());
                record.setWarehouseRecordDetailList(details);
            });
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
