package com.lyf.scm.core.service.stockFront.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.stockFront.ShopService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShopServiceImpl implements ShopService {
	
	@Resource
	private StockRealWarehouseFacade stockRealWarehouseFacade;
	
	@Resource
	private BaseFacade baseFacade;

	/**
	 * 获取所有非门店仓下的门店
	 */
	@Override
	public List<StoreDTO> queryNotShopFactory() {
		List<RealWarehouse> realWarehouseList = stockRealWarehouseFacade.queryNotShopWarehouse();
		List<String> factoryCodes = realWarehouseList.stream().map(RealWarehouse :: getFactoryCode).distinct().collect(Collectors.toList());
        List<StoreDTO> storeList = baseFacade.searchByCodeList(factoryCodes.stream().distinct().collect(Collectors.toList()));
        return storeList;
	}

	/**
	 * 获取所有实仓工厂(区分门店)
	 */
	@Override
	public List<StoreDTO> getRealWarehouseFactory() {
		 return baseFacade.searchByType("B");
	}

}