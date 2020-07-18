package com.lyf.scm.core.service.stockFront;

import java.util.List;

import com.lyf.scm.core.remote.base.dto.StoreDTO;

public interface ShopService {

	/**
	 * 获取所有非门店仓下的门店
	 * 
	 * @return
	 */
	List<StoreDTO> queryNotShopFactory();

	/**
	 * 获取所有实仓工厂(区分门店)
	 * 
	 * @return
	 */
	List<StoreDTO> getRealWarehouseFactory();

}