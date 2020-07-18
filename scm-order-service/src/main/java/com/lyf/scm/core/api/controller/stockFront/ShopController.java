package com.lyf.scm.core.api.controller.stockFront;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.service.stockFront.ShopService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order/v1/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "获取所有非门店仓下的门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryNotShopFactory", method = RequestMethod.GET)
    public Response<List<StoreDTO>> queryNotShopFactory() {
        try {
            List<StoreDTO> list = shopService.queryNotShopFactory();
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
    
    @ApiOperation(value = "获取所有实仓工厂(区分门店)", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/getRealWarehouseFactory", method = RequestMethod.GET)
    public Response getRealWarehouseFactory() {
        try {
            List<StoreDTO> warehouses = shopService.getRealWarehouseFactory();
            return Response.builderSuccess(warehouses);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}