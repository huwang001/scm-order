package com.lyf.scm.core.api.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordCondition;
import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopSaleRecordDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.stockFront.ShopSaleService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 门店零售
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/shopSale")
@Api(tags = {"门店零售"})
public class ShopSaleController {

    @Autowired
    private ShopSaleService shopSaleService;

    @ScmCallLog(systemName = "inner-trade", recordCode = "#shopSaleRecordDTO.outRecordCode")
    @ApiOperation(value = "门店零售出库", nickname = "addShopRetailRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value = "/frontRecord/addShopSaleRecord", method = RequestMethod.POST)
    public Response addShopRetailRecord(@ApiParam(name = "shopTradeRecord", value = "门店零售前置单") @RequestBody @Validated ShopSaleRecordDTO shopSaleRecordDTO) {
        long time = System.currentTimeMillis();
        try {
            shopSaleService.addShopSaleRecord(shopSaleRecordDTO);
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            log.info("门店零售处理结束：单号={},耗时={}", shopSaleRecordDTO.getOutRecordCode(), (System.currentTimeMillis() - time));
        }
    }

    @ScmCallLog(systemName = "inner-trade", recordCode = "#outRecordCode")
    @ApiOperation(value = "取消门店零售出库单", nickname = "cancelShopRetailRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value = "/frontRecord/cancelShopRetailRecord", method = RequestMethod.POST)
    public Response cancelShopRetailRecord(@ApiParam(name = "outRecordCode", value = "交易单号") @RequestParam("outRecordCode") String outRecordCode) {
        long time = System.currentTimeMillis();
        if (StringUtils.isBlank(outRecordCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            shopSaleService.cancelShopSaleRecord(outRecordCode);
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
            log.info("取消门店零售处理结束：单号={},耗时={}", outRecordCode, (System.currentTimeMillis() - time));
        }
    }

    @ApiOperation(value = "查询门店零售出库单", nickname = "query_sale_warehouse_record", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageInfo<SaleWarehouseRecordDTO>> queryByCondition(@ApiParam(name = "condition", value = "查询条件")
                                                                       @RequestBody SaleWarehouseRecordCondition condition) {
        try {
            return Response.builderSuccess(shopSaleService.queryWarehouseRecordList(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "跟据门店销售出库单id查询详情", nickname = "query_sale_warehouse_record_detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/querySaleWarehouseRecordInfo/{recordId}", method = RequestMethod.GET)
    public Response<SaleWarehouseRecordDTO> queryByCondition(@ApiParam(name = "recordId", value = "recordId") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(shopSaleService.querySaleWarehouseRecordInfoById(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
