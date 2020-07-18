package com.lyf.scm.admin.controller.stockFront;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyf.scm.admin.remote.stockFront.dto.StoreDTO;
import com.lyf.scm.admin.remote.stockFront.facade.ShopFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RomeController
@RequestMapping("/order/v1/shop")
public class ShopController {

    @Autowired
    private ShopFacade shopFacade;

    @ApiOperation(value = "获取所有非门店仓下的门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryNotShopFactory", method = RequestMethod.GET)
    public Response<List<StoreDTO>> queryNotShopFactory() {
        try {
            List<StoreDTO> list = shopFacade.queryNotShopFactory();
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
            List<StoreDTO> warehouses = shopFacade.getRealWarehouseFactory();
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