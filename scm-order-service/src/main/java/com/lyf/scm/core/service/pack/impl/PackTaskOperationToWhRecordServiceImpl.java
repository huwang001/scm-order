package com.lyf.scm.core.service.pack.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.WarehouseRecordBatchStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordBusinessTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.enums.pack.PackTypeEnum;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishDetailE;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;
import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.pack.PackTaskOperationToWhRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanglong
 */
@Slf4j
@Service("packTaskOperationToWhRecordService")
public class PackTaskOperationToWhRecordServiceImpl implements PackTaskOperationToWhRecordService {

    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private ItemInfoTool itemInfoTool;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;

    @Override
    public WarehouseRecordE createOutWhRecordByTaskOperation(PackTaskFinishE finishE, List<PackTaskFinishDetailE> finishDetailList) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        WarehouseRecordTypeEnum type = WarehouseRecordTypeEnum.PACKAGE_FINISH_PACK_OUT_RECORD;
        if (PackTypeEnum.UN_PACKAGING.getType().equals(finishE.getPackType())) {
            type = WarehouseRecordTypeEnum.PACKAGE_FINISH_UNPACK_OUT_RECORD;
        }
        String code = orderUtilService.queryOrderCode(type.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(finishE.getOutRealWarehouseId());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.OUT_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(type.getType());
        warehouseRecord.setOutCreateTime(finishE.getPackTime());
        warehouseRecord.setChannelCode(finishE.getChannelCode());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //状态为已出库状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.OUT_ALLOCATION.getStatus());
        warehouseRecord.setFactoryCode(finishE.getOutFactoryCode());
        warehouseRecord.setRealWarehouseCode(finishE.getOutRealWarehouseCode());

        if (finishDetailList != null) {
            List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
            for (PackTaskFinishDetailE detail : finishDetailList) {
                WarehouseRecordDetailE warehouseRecordDetail = createRecordDetailByActualFrontRecord(detail);
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }
        //保存出库单 详情 关系
        addWarehouseRecord(warehouseRecord, finishE);
        return warehouseRecord;
    }

    @Override
    public WarehouseRecordE createInWhRecordByTaskOperation(PackTaskFinishE finishE, List<PackTaskFinishDetailE> finishDetailList) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        WarehouseRecordTypeEnum type = WarehouseRecordTypeEnum.PACKAGE_FINISH_PACK_IN_RECORD;
        if (PackTypeEnum.UN_PACKAGING.getType().equals(finishE.getPackType())) {
            type = WarehouseRecordTypeEnum.PACKAGE_FINISH_UNPACK_IN_RECORD;
        }
        String code = orderUtilService.queryOrderCode(type.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(finishE.getOutRealWarehouseId());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(type.getType());
        warehouseRecord.setOutCreateTime(finishE.getPackTime());
        warehouseRecord.setChannelCode(finishE.getChannelCode());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //状态为已入库状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus());
        warehouseRecord.setFactoryCode(finishE.getOutFactoryCode());
        warehouseRecord.setRealWarehouseCode(finishE.getOutRealWarehouseCode());

        if (finishDetailList != null) {
            List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
            for (PackTaskFinishDetailE detail : finishDetailList) {
                WarehouseRecordDetailE warehouseRecordDetail = createRecordDetailByActualFrontRecord(detail);
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }
        //保存入库单 详情 关系
        addWarehouseRecord(warehouseRecord, finishE);
        return warehouseRecord;
    }

    /**
     * 根据前置单明细-生成后置单明细
     *
     * @param detail
     * @return
     */
    public WarehouseRecordDetailE createRecordDetailByActualFrontRecord(PackTaskFinishDetailE detail) {
        WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
        warehouseRecordDetail.setSkuId(detail.getSkuId());
        warehouseRecordDetail.setSkuCode(detail.getSkuCode());
        warehouseRecordDetail.setPlanQty(detail.getBasicSkuQty());
        warehouseRecordDetail.setActualQty(detail.getBasicSkuQty());
        warehouseRecordDetail.setUnit(detail.getBasicUnit());
        warehouseRecordDetail.setUnitCode(detail.getBasicUnitCode());
        //后置单明细交货单行号 = 前置单明细Id
        if (null != detail.getId()) {
            warehouseRecordDetail.setDeliveryLineNo(String.valueOf(detail.getId()));
        }
        return warehouseRecordDetail;
    }

    /**
     * 保存出库单(入库单) 明细 关系
     *
     * @param warehouseRecord
     * @param frontRecordE
     */
    private void addWarehouseRecord(WarehouseRecordE warehouseRecord, PackTaskFinishE frontRecordE) {
        //保存后置单
        if (warehouseRecord.getSyncTransferStatus() == null) {
            warehouseRecord.setSyncTransferStatus(WarehouseRecordConstant.INIT_TRANSFER);
        }
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecord);

        //保存后置单明细
        List<WarehouseRecordDetailE> recordDetailEList = warehouseRecord.getWarehouseRecordDetailList();
        recordDetailEList.forEach(record -> {
            record.setWarehouseRecordId(warehouseRecord.getId());
            record.setRecordCode(warehouseRecord.getRecordCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(recordDetailEList);

        //保存前置单 + 后置单关系
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecord.getId());
        relation.setFrontRecordId(frontRecordE.getId());
        relation.setFrontRecordType(frontRecordE.getRecordType());
        relation.setRecordCode(warehouseRecord.getRecordCode());
        relation.setFrontRecordCode(frontRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }
}
