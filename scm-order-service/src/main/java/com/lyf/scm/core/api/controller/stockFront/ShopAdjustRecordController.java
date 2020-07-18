package com.lyf.scm.core.api.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.stockFront.AdjustForetasteDetailService;
import com.lyf.scm.core.service.stockFront.AdjustForetasteService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/order/v1/adjust")
@Api(tags = {"门店试吃调整单"})
public class ShopAdjustRecordController {

    @Autowired
    private AdjustForetasteService adjustForetasteService;
    @Autowired
    private AdjustForetasteDetailService adjustForetasteDetailService;

    @ScmCallLog(systemName = "inner-trade", recordCode = "#frontRecord.outRecordCode")
    @ApiOperation(value = "门店试吃调整单创建", nickname = "ShopForetasteRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/frontRecord/addShopForetasteRecord", method = RequestMethod.POST)
    public Response addShopForetasteRecord(@ApiParam(name = "frontRecord", value = "门店试吃调整单创建") @RequestBody ShopAdjustRecordDTO frontRecord) {
        try {
            adjustForetasteService.addShopForetasteRecord(frontRecord);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询门店试吃调整单", nickname = "find_shop_adjust_record_condition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/pageShopAdjustForetaste", method = RequestMethod.POST)
    public Response<PageInfo<ShopAdjustRecordDTO>> pageShopAdjustForetaste(
            @ApiParam(name = "shopAdjustRecordDTO", value = "试吃调整单条件对象") @RequestBody @Valid ShopAdjustRecordDTO queryOrderDTO,
            @ApiParam(name = "pageNum", value = "当前页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam("pageSize") Integer pageSize) {
        try {
            PageInfo<ShopAdjustRecordDTO> salesReturnRecordPageInfo = adjustForetasteService.findShopForetasteCondition(queryOrderDTO, pageNum, pageSize);
            return Response.builderSuccess(salesReturnRecordPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据试吃单id查询门店调整单详情", nickname = "getAdjustForetasteByRecordId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/getAdjustForetasteByRecordId", method = RequestMethod.GET)
    public Response getAdjustForetasteByRecordId(@RequestParam("recordId") Long recordId) {
        try {
            ShopAdjustRecordDTO shopAdjustRecordDTO = adjustForetasteDetailService.getAdjustForetasteByRecordId(recordId);
            return Response.builderSuccess(shopAdjustRecordDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
