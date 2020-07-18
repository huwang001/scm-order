package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import com.lyf.scm.core.domain.entity.stockFront.*;
import com.lyf.scm.core.mapper.stockFront.FrontWarehouseRecordRelationMapper;
import com.lyf.scm.core.service.stockFront.FrontWarehouseRecordRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service("frontWarehouseRecordRelationService")
public class FrontWarehouseRecordRelationServiceImpl implements FrontWarehouseRecordRelationService {

    @Resource
    private FrontWarehouseRecordRelationMapper frontWarehouseRecordRelationMapper;

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, AdjustForetasteE frontRecord) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecord.getId());
        relation.setFrontRecordId(frontRecord.getId());
        relation.setFrontRecordType(frontRecord.getRecordType());
        relation.setRecordCode(warehouseRecord.getRecordCode());
        relation.setFrontRecordCode(frontRecord.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, ShopInventoryE frontRecordE) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecord.getId());
        relation.setFrontRecordId(frontRecordE.getId());
        relation.setFrontRecordType(frontRecordE.getRecordType());
        relation.setRecordCode(warehouseRecord.getRecordCode());
        relation.setFrontRecordCode(frontRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, ShopSaleE frontRecordE) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecord.getId());
        relation.setFrontRecordId(frontRecordE.getId());
        relation.setFrontRecordType(frontRecordE.getRecordType());
        relation.setRecordCode(warehouseRecord.getRecordCode());
        relation.setFrontRecordCode(frontRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, SalesReturnE frontRecordE) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecord.getId());
        relation.setFrontRecordId(frontRecordE.getId());
        relation.setFrontRecordType(frontRecordE.getRecordType());
        relation.setRecordCode(warehouseRecord.getRecordCode());
        relation.setFrontRecordCode(frontRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, ShopAllocationE frontRecordE) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecord.getId());
        relation.setFrontRecordId(frontRecordE.getId());
        relation.setFrontRecordType(frontRecordE.getRecordType());
        relation.setRecordCode(warehouseRecord.getRecordCode());
        relation.setFrontRecordCode(frontRecordE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecordE, OrderReturnE orderReturn) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecordE.getId());
        relation.setFrontRecordId(orderReturn.getId());
        relation.setFrontRecordType(orderReturn.getRecordType());
        relation.setRecordCode(warehouseRecordE.getRecordCode());
        relation.setFrontRecordCode(orderReturn.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecordE, ShopReturnE shopReturnE) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecordE.getId());
        relation.setFrontRecordId(shopReturnE.getId());
        relation.setFrontRecordType(shopReturnE.getRecordType());
        relation.setRecordCode(warehouseRecordE.getRecordCode());
        relation.setFrontRecordCode(shopReturnE.getRecordCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }

    @Override
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecordE, OrderE orderE) {
        FrontWarehouseRecordRelationE relation = new FrontWarehouseRecordRelationE();
        relation.setWarehouseRecordId(warehouseRecordE.getId());
        relation.setFrontRecordId(orderE.getId());
        relation.setFrontRecordType(orderE.getRecordType());
        relation.setRecordCode(warehouseRecordE.getRecordCode());
        relation.setFrontRecordCode(orderE.getOrderCode());
        frontWarehouseRecordRelationMapper.insertFrontWarehouseRecordRelation(relation);
    }
}
