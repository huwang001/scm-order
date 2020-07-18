package com.lyf.scm.core.api.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.service.common.RealWarehouseAllocationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 实仓调拨
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/realWarehouse/allocation")
@Api(tags={"实仓调拨"})
public class RealWarehouseAllocationController {

    @Resource
    private RealWarehouseAllocationService realWarehouseAllocationService;

    @ApiOperation(value = "根据工厂编号和仓库类型查询对应仓库", nickname = "queryRealWarehouseByFactoryCodeAndType", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryRealWarehouseByFactoryCodeAndType", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndType(@ApiParam(name = "factoryCode", value = "工厂编码") @RequestParam(name = "factoryCode") String factoryCode
            , @ApiParam(name = "type", value = "查询类型") @RequestParam(name = "type") Integer type){
        try {
            List<RealWarehouse> list = realWarehouseAllocationService.queryRealWarehouseByFactoryCodeAndType(factoryCode,type);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "实仓调拨", nickname = "realWarehouseAllocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/realWarehouseAllocation", method = RequestMethod.POST)
    @ScmCallLog(systemName = "scm-core-service",recordCode = "#orderCode")
    public Response realWarehouseAllocation(@ApiParam(name = "orderCode", value = "预约单号") @RequestParam(name = "orderCode") String orderCode,
    		@ApiParam(name = "allotRealWarehouseCode", value = "仓库编号") @RequestParam(name = "allotRealWarehouseCode") String allotRealWarehouseCode, @RequestParam("userId") Long userId){
        try {
        	log.info("预约单实仓调拨 <<< {}",orderCode +"_"+ allotRealWarehouseCode +"_"+ userId);
            realWarehouseAllocationService.realWarehouseAllocation(orderCode,allotRealWarehouseCode, userId);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "实仓调拨单出库通知", nickname = "realWarehouseAllocationOutNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/realWarehouseAllocationOutNotify", method = RequestMethod.POST)
    @ScmCallLog(systemName = "stock-core-service",recordCode = "#allotCode")
    public Response realWarehouseAllocationOutNotify(@ApiParam(name = "allotCode", value = "调拨单号") @RequestParam(name = "allotCode") String allotCode){
        try {
            realWarehouseAllocationService.realWarehouseAllocationOutNotify(allotCode);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "实仓调拨单入库通知", nickname = "realWarehouseAllocationInNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/realWarehouseAllocationInNotify", method = RequestMethod.POST)
    @ScmCallLog(systemName = "stock-core-service",recordCode = "#allotCode")
    public Response realWarehouseAllocationInNotify(@ApiParam(name = "allotCode", value = "调拨单号") @RequestParam(name = "allotCode") String allotCode){
        try {
            realWarehouseAllocationService.realWarehouseAllocationInNotify(allotCode);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "团购仓出库发货回调", nickname = "deliverOutNotifyToGroupPurchase", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/deliverOutNotifyToGroupPurchase", method = RequestMethod.POST)
    @ScmCallLog(systemName = "stock-core-service",recordCode = "#outRecordCode")
    public Response deliverOutNotifyToGroupPurchase(@ApiParam(name = "outRecordCode", value = "预约单号") @RequestParam(name = "outRecordCode") String outRecordCode){
        try {
            realWarehouseAllocationService.deliverOutNotifyToGroupPurchase(outRecordCode);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }
}
