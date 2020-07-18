package com.lyf.scm.core.remote.sap;

import com.lyf.scm.core.api.dto.stockFront.ShopInventoryDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "stock-sap-proxy")
public interface SapRemoteService {

    /**
     * 门店盘点 下发SAP
     */
    @RequestMapping(value = "/sap/v1/shop_inventory/pushShopInventoryList", method = RequestMethod.POST)
    Response<Boolean> pushShopInventoryList(@RequestBody List<ShopInventoryDTO> shopInventoryList);
}
