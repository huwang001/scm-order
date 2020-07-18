package com.lyf.scm.core.service.order.impl;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.constants.WarehouseRecordConstant;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.util.validate.AlikAssert;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordDetailMapper;
import com.lyf.scm.core.mapper.stockFront.WarehouseRecordMapper;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.order.OrderToWareHouseRecordService;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 创建出库单service
 * @Author wuyuanhang
 * @Date 2020/7/1
 */
@Slf4j
@Service("orderToWareHouseRecordService")
public class OrderToWareHouseRecordServiceImpl implements OrderToWareHouseRecordService {

    @Resource
    private OrderUtilService orderUtilService;
    @Resource
    private WarehouseRecordMapper warehouseRecordMapper;
    @Resource
    private WarehouseRecordDetailMapper warehouseRecordDetailMapper;
    @Autowired
    private FrontWarehouseRecordRelationService frontWarehouseRecordRelationService;
    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;


    @Override
    public WarehouseRecordE createOutRecordByFrontRecord(OrderE orderE) {
        WarehouseRecordE warehouseRecord = new WarehouseRecordE();
        //根据预约单生成出库单数据
        String code = orderUtilService.queryOrderCode(WarehouseRecordTypeEnum.RESERVATION_DO_RECORD.getCode());
        warehouseRecord.setRecordCode(code);

        RealWarehouse realWarehouse = stockRealWarehouseFacade.queryByWarehouseCodeAndFactoryCode(orderE.getRealWarehouseCode(), orderE.getFactoryCode());
        AlikAssert.isNotNull(realWarehouse, ResCode.ORDER_ERROR_6005, ResCode.ORDER_ERROR_6005_DESC);
        warehouseRecord.setRealWarehouseId(realWarehouse.getId());

        warehouseRecord.setRealWarehouseCode(orderE.getRealWarehouseCode());
        warehouseRecord.setFactoryCode(orderE.getFactoryCode());
        warehouseRecord.setRecordStatus(WarehouseRecordStatusEnum.INIT.getStatus());
        warehouseRecord.setBusinessType(WarehouseRecordTypeEnum.RESERVATION_DO_RECORD.getBusinessType());
        warehouseRecord.setRecordType(WarehouseRecordTypeEnum.RESERVATION_DO_RECORD.getType());
        warehouseRecord.setCmpStatus(WarehouseRecordConstant.INIT_CMP);
        warehouseRecord.setOutCreateTime(orderE.getCreateTime());
        warehouseRecord.setDeliveryTime(orderE.getCreateTime());
        List<OrderDetailE> orderDetailEList = orderE.getOrderDetailEList();

        List<WarehouseRecordDetailE> warehouseRecordDetailList = new ArrayList<>();
        WarehouseRecordDetailE warehouseRecordDetail = null;
        if (orderDetailEList != null) {
            for (OrderDetailE orderDetailE : orderDetailEList) {
                //前置单明细生成后置单明细
                warehouseRecordDetail = this.createRecordDetailByActualFrontRecord(orderDetailE);
                warehouseRecordDetail.setRealWarehouseId(warehouseRecord.getRealWarehouseId());
                warehouseRecordDetailList.add(warehouseRecordDetail);
            }
        }
        warehouseRecord.setWarehouseRecordDetailList(warehouseRecordDetailList);

        //创建门店调拨出库单
        this.saveWareHouseRecord(warehouseRecord, orderE);
        return warehouseRecord;
    }

    /**
     * 根据前置单生成单据明细(实际出库数就是计划数)
     */
    private WarehouseRecordDetailE createRecordDetailByActualFrontRecord(OrderDetailE orderDetailE) {
        WarehouseRecordDetailE warehouseRecordDetail = new WarehouseRecordDetailE();
        warehouseRecordDetail.setSkuId(orderDetailE.getSkuId());
        warehouseRecordDetail.setSkuCode(orderDetailE.getSkuCode());
        warehouseRecordDetail.setPlanQty(orderDetailE.getOrderQty());
        warehouseRecordDetail.setActualQty(BigDecimal.ZERO);
        warehouseRecordDetail.setUnit(orderDetailE.getUnit());
        warehouseRecordDetail.setUnitCode(orderDetailE.getUnitCode());
        return warehouseRecordDetail;
    }

    /**
     * 保存后置单 主表
     *
     * @param warehouseRecord
     * @param orderE
     * @return
     */
    public boolean saveWareHouseRecord(WarehouseRecordE warehouseRecord, OrderE orderE) {
        //保存后置单主表信息
        warehouseRecordMapper.insertWarehouseRecord(warehouseRecord);

        //保存后置单 明细表 数据
        warehouseRecord.getWarehouseRecordDetailList().forEach(record -> {
            record.setWarehouseRecordId(warehouseRecord.getRealWarehouseId());
            record.setRecordCode(warehouseRecord.getRecordCode());
            record.setRealWarehouseId(warehouseRecord.getRealWarehouseId());
            record.setChannelCode(warehouseRecord.getChannelCode());
        });
        warehouseRecordDetailMapper.insertWarehouseRecordDetails(warehouseRecord.getWarehouseRecordDetailList());

        //保存前置单和出库单一对一关系
        frontWarehouseRecordRelationService.saveAddFrontRecordAndWarehouseRelation(warehouseRecord, orderE);
        return Boolean.TRUE;
    }
}
