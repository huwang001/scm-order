package com.lyf.scm.admin.remote.stockFront.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lyf.scm.admin.remote.stockFront.ShopRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.StoreDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShopFacade {

    @Resource
    private ShopRemoteService shopRemoteService;

	public List<StoreDTO> queryNotShopFactory() {
		Response<List<StoreDTO>> response = shopRemoteService.queryNotShopFactory();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

	public List<StoreDTO> getRealWarehouseFactory() {
		Response<List<StoreDTO>> response = shopRemoteService.getRealWarehouseFactory();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

}