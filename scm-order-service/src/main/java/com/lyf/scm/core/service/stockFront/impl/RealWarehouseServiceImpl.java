package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.stockFront.RealWarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class RealWarehouseServiceImpl implements RealWarehouseService {

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;

    @Override
    public List<RealWarehouse> queryRealWarehouseByFactoryCode(String factoryCode) {
        List<RealWarehouse> list = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodes(Arrays.asList(factoryCode));
        return list;
    }

    @Override
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeNoShop(String factoryCode) {
        List<RealWarehouse> list = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodesNoShop(factoryCode);
        return list;
    }


    @Override
    public List<RealWarehouse> queryNotShopWarehouse() {
        List<RealWarehouse> list = stockRealWarehouseFacade.queryNotShopWarehouse();
        return list;
    }

    @Override
    public List<RealWarehouse> queryNotShopWarehouseByType(Integer type) {
        List<RealWarehouse> list = stockRealWarehouseFacade.queryNotShopWarehouseByType(type);
        return list;
    }

    @Override
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndRWType(String factoryCode, Integer realWarehouseType) {
        List<RealWarehouse> list = stockRealWarehouseFacade.queryRealWarehouseByFactoryCodeAndRWType(factoryCode, realWarehouseType);
        return list;
    }

}