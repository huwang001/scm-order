package com.lyf.scm.core.api.controller.stockFront;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.InventoryRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryPageDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.stockFront.ShopInventoryService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RomeController
@RequestMapping("/order/v1/shopInventory")
@Api(tags = {"门店盘点"})
public class ShopInventoryController {

    @Autowired
    private ShopInventoryService shopInventoryService;

    @ApiOperation(value = "查询未处理盘点单详情列表", nickname = "query_init_shop_inventory_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/frontRecord/queryInitShopInventoryRecords", method = RequestMethod.GET)
    public Response<List<Long>> queryInitShopInventoryRecords(Integer startPage, Integer endPage) {
        try {
            List<Long> list = shopInventoryService.queryInitShopInventoryRecords(startPage, endPage);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_7201, ResCode.ORDER_ERROR_7201_DESC);
        }
    }

    @ApiOperation(value = "批量处理盘点单", nickname = "handleShopInventoryRecords", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/frontRecord/handleShopInventoryRecords", method = RequestMethod.POST)
    public Response<String> handleShopInventoryRecords(@ApiParam(name = "前置单id", value = "id") @RequestParam Long id) {
        try {
            shopInventoryService.handleShopInventoryRecords(id);
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_7201, ResCode.ORDER_ERROR_7201_DESC);
        }
    }

    @ApiOperation(value = "批量保存门店盘点单", nickname = "ShopInventoryRecords", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/frontRecord/addShopInventoryRecords", method = RequestMethod.POST)
    public Response<String> addShopInventoryRecords(@ApiParam(name = "frontRecord", value = "门店盘点单") @RequestBody @Validated List<InventoryRecordDTO> frontRecords) {
        try {
            log.info("批量盘点单数据addShopInventoryRecords：" + JSON.toJSONString(frontRecords));
            shopInventoryService.addShopInventoryRecords(frontRecords);
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_7201, ResCode.ORDER_ERROR_7201_DESC);
        }
    }

    @ApiOperation(value = "查询盘点单列表", nickname = "query_shop_inventory_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/frontRecord/queryShopInventoryList", method = RequestMethod.POST)
    public Response<PageInfo<ShopInventoryPageDTO>> queryShopInventoryList(@ApiParam(name = "shopInventory", value = "dto") @RequestBody ShopInventoryPageDTO frontRecord) {
        try {
            PageInfo<ShopInventoryPageDTO> personPageInfo = shopInventoryService.queryShopInventoryList(frontRecord);
            return Response.builderSuccess(personPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_7201, ResCode.ORDER_ERROR_7201_DESC);
        }
    }

    @ApiOperation(value = "查询盘点单详情列表", nickname = "query_shop_inventory_detail_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/frontRecord/queryShopInventoryDetailList", method = RequestMethod.GET)
    public Response<List<ShopInventoryDetailDTO>> queryShopInventoryDetailList(@ApiParam(name = "前置单id", value = "frontRecordId") @RequestParam Long frontRecordId) {
        try {
            List<ShopInventoryDetailDTO> list = shopInventoryService.queryShopInventoryDetailList(frontRecordId);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_7201, ResCode.ORDER_ERROR_7201_DESC);
        }
    }
}
