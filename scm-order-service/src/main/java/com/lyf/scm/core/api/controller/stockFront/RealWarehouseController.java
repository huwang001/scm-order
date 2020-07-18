package com.lyf.scm.core.api.controller.stockFront;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.service.stockFront.RealWarehouseService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order/v1/real_warehouse")
@Api(tags = {"仓库查询管理"})
public class RealWarehouseController {

    @Autowired
    private RealWarehouseService realWarehouseService;

    @ApiOperation(value = "根据工厂code查询仓库信息-包含门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryRealWarehouseByFactoryCode", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCode(@RequestBody String factoryCode) {
        // 字符串非空判断
        if (StringUtils.isBlank(factoryCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseService.queryRealWarehouseByFactoryCode(factoryCode);
            return Response.builderSuccess(realWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据工厂code查询仓库信息-不包含门店", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryRealWarehouseByFactoryCodeNoShop", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeNoShop(@RequestBody String factoryCode) {
        // 字符串非空判断
        if (StringUtils.isBlank(factoryCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseService.queryRealWarehouseByFactoryCodeNoShop(factoryCode);
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
            List<RealWarehouse> realWarehouseList = realWarehouseService.queryNotShopWarehouse();
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
            List<RealWarehouse> realWarehouseList = realWarehouseService.queryNotShopWarehouseByType(realWarehouseType);
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
    @RequestMapping(value = "/queryRealWarehouseByFactoryCodeAndRWType", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndRWType(@RequestParam("factoryCode") String factoryCode,
                                                                                  @RequestParam("realWarehouseType") Integer realWarehouseType) {

        // 字符串非空判断
        if (StringUtils.isBlank(factoryCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            List<RealWarehouse> realWarehouseList = realWarehouseService.queryRealWarehouseByFactoryCodeAndRWType(factoryCode, realWarehouseType);
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