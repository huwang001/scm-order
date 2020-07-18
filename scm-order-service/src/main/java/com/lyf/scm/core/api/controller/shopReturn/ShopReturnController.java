package com.lyf.scm.core.api.controller.shopReturn;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.shopReturn.ShopReturnDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseToBRecord;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.shopReturn.ShopReturnService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RomeController
@RequestMapping("/order/v1/shopReturn")
@Api(tags = {"门店退货单接口管理-交易中心调用"})
public class ShopReturnController {
    @Resource
    private ShopReturnService shopReturnService;

    @ApiOperation(value = "同步门店退货申请", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = WarehouseToBRecord.class)
    @RequestMapping(value = "/addShopReturnApply", method = RequestMethod.POST)
    @ScmCallLog(systemName = "", recordCode = "#shopReturn.outRecordCode")
    public Response addShopReturnApply(@ApiParam(name = "shopReturn", value = "门店退货") @RequestBody @Validated ShopReturnDTO shopReturn) {
        log.info("门店退货申请尝试" + shopReturn.toString());
        String message = "";
        boolean isSucc = false;
        try {
            shopReturnService.addShopReturn(shopReturn);
            isSucc = true;
            message = "200";
            return ResponseMsg.SUCCESS.buildMsg();
        } catch (RomeException e) {
            message = e.getMessage();
            log.error("addShopReturnApply调用 : " + e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            message = e.getMessage();
            log.error("addShopReturnApply调用 : " + e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "job查询-待推送交易的-门店退货单", nickname = "queryUnPushTradeShopReturnRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryUnPushTradeShopReturnRecord", method = RequestMethod.POST)
    public Response queryUnPushTradeShopReturnRecord(@RequestParam("page") Integer page, @RequestParam("maxResult") Integer maxResult) {
        try {
            List<String> frontRecordCodeList = shopReturnService.queryUnPushTradeShopReturnRecord(page, maxResult);
            return Response.builderSuccess(frontRecordCodeList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "job门店退货单-推送交易", nickname = "pushTradeShopReturnRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/pushTradeShopReturnRecord", method = RequestMethod.POST)
    public Response<String> pushTradeShopReturnRecord(@ApiParam(name = "frontRecordCode", value = "前置单编码") @RequestParam("frontRecordCode") String frontRecordCode) {
        try {
            shopReturnService.handlePushTradeShopReturnRecord(frontRecordCode);
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value="门店退货取消",nickname = "shopReturnCancel", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200,message = "success")
    @RequestMapping(value="/Cancel",method = RequestMethod.POST)
    public Response shopReturnCancel(@ApiParam(name="outRecordCode",value="交易中心退货编码") @RequestParam("outRecordCode") String outRecordCode){
        try{
            log.info("门店退货取消,入参 >>>{}", JSON.toJSON(outRecordCode));
            Integer cancelResult = shopReturnService.shopReturnCancel(outRecordCode);
            return Response.builderSuccess(cancelResult);
        }catch(RomeException e){
            log.info(e.getMessage(),e);
            return Response.builderFail(e.getCode(),e.getMessage());
        }catch(Exception e){
           log.info(e.getMessage(),e);
           return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
