package com.lyf.scm.core.api.controller.stockFront;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordParamDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnWarehouseRecordDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.stockFront.SalesReturnService;
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
 * 门店零售退货
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/salesReturn")
@Api(tags = {"门店零售退货"})
public class SalesReturnController {

    @Autowired
    private SalesReturnService salesReturnService;

    @ScmCallLog(systemName = "inner-trade", recordCode = "#frontRecord.outRecordCode")
    @ApiOperation(value = "门店退货单", nickname = "SalesReturnRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/addSalesReturnRecord", method = RequestMethod.POST)
    public Response addSalesReturnRecord(@ApiParam(name = "salesReturnRecord", value = "退货订单") @RequestBody @Validated SalesReturnRecordDTO frontRecord) {
        try {
            log.info("======================================================门店退货单 start=======:入参" + JSON.toJSONString(frontRecord));
            salesReturnService.addShopSaleReturnRecord(frontRecord);
            log.info("======================================================门店退货单 SUCCESS=======");
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ScmCallLog(systemName = "inner-trade", recordCode = "#outRecordCode")
    @ApiOperation(value = "取消门店零售出库单", nickname = "cancelSalesReturnRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @RequestMapping(value = "/cancelSalesReturnRecord", method = RequestMethod.POST)
    public Response cancelSalesReturnRecord(@ApiParam(name = "outRecordCode", value = "交易单号") @RequestParam("outRecordCode") String outRecordCode) {
        long time = System.currentTimeMillis();
        if (StringUtils.isBlank(outRecordCode)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        try {
            salesReturnService.cancelShopSaleReturnRecord(outRecordCode);
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

    @ApiOperation(value = "根据条件查询退货单", nickname = "find_sales_return_record", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/condition", method = RequestMethod.POST)
    public Response<PageInfo<SalesReturnWarehouseRecordDTO>> findBySalesReturnRecordCondition(@RequestBody SalesReturnRecordParamDTO paramDTO) {
        try {
            PageInfo<SalesReturnWarehouseRecordDTO> salesReturnRecordPageInfo = salesReturnService.findBySalesReturnRecordCondition(paramDTO);
            return Response.builderSuccess(salesReturnRecordPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据退货单id查询退货详情", nickname = "queryDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryDetail/{recordId}", method = RequestMethod.GET)
    public Response<SalesReturnWarehouseRecordDTO> querySaleReturnWarehouseRecordInfoById(@PathVariable Long recordId) {
        try {
            SalesReturnWarehouseRecordDTO salesReturnRecordPageInfo = salesReturnService.querySaleReturnWarehouseRecordInfoById(recordId);
            return Response.builderSuccess(salesReturnRecordPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
