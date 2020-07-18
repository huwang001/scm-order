package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.*;
import com.lyf.scm.core.domain.entity.stockFront.SalesReturnDetailE;
import com.lyf.scm.core.domain.entity.stockFront.SalesReturnE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.item.ItemInfoTool;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import com.lyf.scm.core.service.stockFront.SalesReturnToWareHouseRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhanlong
 */
@Slf4j
@Service("salesReturnToWareHouseRecordService")
public class SalesReturnToWareHouseRecordServiceImpl implements SalesReturnToWareHouseRecordService {

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
    public WarehouseRecordE createWarehouseRecordBySalesReturn(SalesReturnE frontRecordE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        //根据前置单生成入库单数据
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.SHOP_RETURN_GOODS_WAREHOUSE_RECORD.getCode());
        warehouseRecord.setRecordCode(code);
        warehouseRecord.setRealWarehouseId(frontRecordE.getInRealWarehouseId());
        warehouseRecord.setFactoryCode(frontRecordE.getInFactoryCode());
        warehouseRecord.setRealWarehouseCode(frontRecordE.getInRealWarehouseCode());
        warehouseRecord.setBusinessType(WarehouseRecordBusinessTypeEnum.IN_WAREHOUSE_RECORD.getType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.SHOP_RETURN_GOODS_WAREHOUSE_RECORD.getType());
        warehouseRecord.setChannelCode(frontRecordE.getChannelCode());
        warehouseRecord.setMerchantId(frontRecordE.getMerchantId());
        warehouseRecord.setOutCreateTime(frontRecordE.getOutCreateTime());
        warehouseRecord.setReason(frontRecordE.getReason() == null ? "" : frontRecordE.getReason());
        warehouseRecord.setUserCode(frontRecordE.getUserCode() == null ? "" : frontRecordE.getUserCode());
        warehouseRecord.setMobile(frontRecordE.getMobile() == null ? "" : frontRecordE.getMobile());
        warehouseRecord.setBatchStatus(WarehouseRecordBatchStatusEnum.INIT.getStatus());
        //退货单为完成状态
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.IN_ALLOCATION.getStatus());

        List<SalesReturnDetailE> frontRecordDetails = frontRecordE.getFrontRecordDetails();
        if (frontRecordDetails != null) {
            List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
            for (SalesReturnDetailE detailE : frontRecordDetails) {
                warehouseRecordDetailList.add(createRecordDetailByFrontRecord(detailE));
            }
            //设置skuCode和skuID
            itemInfoTool.convertSkuCode(warehouseRecordDetailList);
            warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);
        }

        //创建门店销售入库单
        saveWarehouseRecordInfo(warehouseRecord, frontRecordE);
        return warehouseRecord;
    }

    /**
     * 根据前置单明细 生成后置单明细
     *
     * @param detailE
     * @return
     */
    private WarehouseRecordDetailE createRecordDetailByFrontRecord(SalesReturnDetailE detailE) {
        WarehouseRecordDetailE warehouseRecordDetailE = new WarehouseRecordDetailE();
        warehouseRecordDetailE.setSkuId(detailE.getSkuId());
        warehouseRecordDetailE.setSkuCode(detailE.getSkuCode());
        //此处设置计划数量和实际数量一致，wms回调再更新实际数量，无需wms回调的业务，直接设置成一致即可
        warehouseRecordDetailE.setPlanQty(detailE.getBasicSkuQty());
        warehouseRecordDetailE.setUnit(detailE.getBasicUnit());
        warehouseRecordDetailE.setUnitCode(detailE.getBasicUnitCode());
        warehouseRecordDetailE.setActualQty(detailE.getBasicSkuQty());
        //后置单明细交货单行号 = 前置单明细Id
        warehouseRecordDetailE.setDeliveryLineNo(String.valueOf(detailE.getId()));
        return warehouseRecordDetailE;
    }

    /**
     * 保存后置单信息 明细 关系
     *
     * @param warehouseRecord
     */
    @Override
    public void saveWarehouseRecordInfo(WarehouseRecordE warehouseRecord, SalesReturnE frontRecordE) {
        if (StringUtils.isBlank(warehouseRecord.getReason())) {
            warehouseRecord.setReason("");
        }
        if (StringUtils.isBlank(warehouseRecord.getUserCode())) {
            warehouseRecord.setUserCode("");
        }
        if (StringUtils.isBlank(warehouseRecord.getMobile())) {
            warehouseRecord.setMobile("");
        }
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

        //存储前置单与仓库单关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecord, frontRecordE);
    }
}
