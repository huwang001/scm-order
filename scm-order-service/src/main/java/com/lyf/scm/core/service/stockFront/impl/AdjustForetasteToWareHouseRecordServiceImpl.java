package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteDetailE;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.AdjustForetasteToWareHouseRecordService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanlong
 */
@Slf4j
@Service("adjustForetasteToWareHouseRecordService")
public class AdjustForetasteToWareHouseRecordServiceImpl implements AdjustForetasteToWareHouseRecordService {

    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;
    @Resource
    private OrderUtilService orderUtilService;

    @Override
    public WarehouseRecordE createWarehouseRecordByAdjustForetaste(AdjustForetasteE frontRecord) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        //根据前置单生成出库单数据
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_FORETASTE_OUT_WAREHOUSE_RECORD.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(frontRecord.getRealWarehouseId());
        warehouseRecord.setFactoryCode(frontRecord.getFactoryCode());
        warehouseRecord.setRealWarehouseCode(frontRecord.getRealWarehouseCode());
        warehouseRecord.setMerchantId(frontRecord.getMerchantId());
        warehouseRecord.setOutCreateTime(frontRecord.getOutCreateTime());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.SHOP_FORETASTE_OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.NEED_TRANSFER);
        warehouseRecord.setDeliveryTime(frontRecord.getOutCreateTime());
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
        warehouseRecord.setChannelCode(frontRecord.getChannelCode());

        List<AdjustForetasteDetailE> shopForetasteRecordDetailEList = frontRecord.getFrontRecordDetails();
        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
        WarehouseRecordDetailE warehouseRecordDetail = null;
        if (null != shopForetasteRecordDetailEList && shopForetasteRecordDetailEList.size() > 0) {
            for (AdjustForetasteDetailE shopForetasteRecordDetailE : shopForetasteRecordDetailEList) {
                //前置单明细 生成 后置单明细
                warehouseRecordDetail = createRecordDetailByFrontRecord(shopForetasteRecordDetailE);
                //后置单明细设置交货单行号 = 前置单明细Id
                warehouseRecordDetail.setDeliveryLineNo(String.valueOf(shopForetasteRecordDetailE.getId()));
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
        }
        //设置skuCode和skuID
        itemInfoTool.convertSkuCode(warehouseRecordDetailList);
        warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);
        //创建门店试吃出库单
        saveWareHouseRecord(warehouseRecord, frontRecord);
        return warehouseRecord;
    }

    /**
     * 根据前置单生成单据明细
     */
    private WarehouseRecordDetailE createRecordDetailByFrontRecord(AdjustForetasteDetailE adjustForetasteDetailE) {
        WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
        warehouseRecordDetailE.setSkuId(adjustForetasteDetailE.getSkuId());
        warehouseRecordDetailE.setSkuCode(adjustForetasteDetailE.getSkuCode());
        //此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
        warehouseRecordDetailE.setPlanQty(adjustForetasteDetailE.getBasicSkuQty());
        warehouseRecordDetailE.setUnit(adjustForetasteDetailE.getBasicUnit());
        warehouseRecordDetailE.setUnitCode(adjustForetasteDetailE.getBasicUnitCode());
        warehouseRecordDetailE.setActualQty(adjustForetasteDetailE.getBasicSkuQty());
        return warehouseRecordDetailE;
    }

    /**
     * 保存后置单 主表
     *
     * @param warehouseRecord
     * @param frontRecord
     * @return
     */
    private boolean saveWareHouseRecord(WarehouseRecordE warehouseRecord, AdjustForetasteE frontRecord) {
        //保存出库单
        if (warehouseRecord.getSyncTransferStatus() == null) {
            warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
        }
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

        //保存前置单和出库单关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecord, frontRecord);
        return Boolean.TRUE;
    }
}
