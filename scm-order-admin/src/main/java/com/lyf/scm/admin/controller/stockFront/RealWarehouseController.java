package com.lyf.scm.admin.controller.stockFront;

import java.util.List;

import javax.annotation.Resource;

import com.lyf.scm.admin.remote.stockFront.dto.StoreDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.stockFront.facade.RealWarehouseFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/order/v1/real_warehouse")
public class RealWarehouseController {

    @Resource
    private RealWarehouseFacade realWarehouseFacade;

    @ApiOperation(value = "根据工厂code查询仓库信息-包含门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryRealWarehouseByFactoryCode", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCode(@RequestBody String factoryCode) {
        // 字符串非空判断
        if (StringUtils.isBlank(factoryCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseFacade.queryRealWarehouseByFactoryCode(factoryCode);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据工厂code查询仓库信息-非门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryRealWarehouseByFactoryCodeNoShop", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeNoShop(@RequestBody String factoryCode) {
        // 字符串非空判断
        if (StringUtils.isBlank(factoryCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseFacade.queryRealWarehouseByFactoryCodeNoShop(factoryCode);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "查询仓库信息-不包含门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryNotShopWarehouse", method = RequestMethod.GET)
    public Response<List<RealWarehouse>> queryNotShopWarehouse() {
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseFacade.queryNotShopWarehouse();
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据仓库类型查询仓库信息-不包含门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryNotShopWarehouseByType", method = RequestMethod.GET)
    public Response<List<RealWarehouse>> queryNotShopWarehouseByType(@RequestParam("realWarehouseType") Integer realWarehouseType) {
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseFacade.queryNotShopWarehouseByType(realWarehouseType);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据工厂code和仓库类型查询仓库信息", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryRealWarehouseByFactoryCodeAndRWType", method = RequestMethod.GET)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndRWType(@RequestParam("factoryCode") String factoryCode,
                                                                                  @RequestParam("realWarehouseType") Integer realWarehouseType) {
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseFacade.queryRealWarehouseByFactoryCodeAndRWType(factoryCode, realWarehouseType);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


}