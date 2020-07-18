package com.lyf.scm.admin.remote.stockFront;

import com.lyf.scm.admin.remote.stockFront.dto.StoreDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
@FeignClient(value="scm-order-service")
public interface ShopRemoteService {

	@RequestMapping(value = "/order/v1/shop/queryNotShopFactory", method = RequestMethod.GET)
	Response<List<StoreDTO>> queryNotShopFactory();

	@RequestMapping(value = "/order/v1/shop/getRealWarehouseFactory", method = RequestMethod.GET)
	Response<List<StoreDTO>> getRealWarehouseFactory();

}