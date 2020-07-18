package com.lyf.scm.core.api.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordPageDTO;
import com.lyf.scm.core.service.stockFront.WarehouseRecordCommService;
import com.lyf.scm.core.service.stockFront.WarehouseRecordService;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RomeController
@RequestMapping("/order/v1/warehouse_record")
@Api(tags = {"出入库单管理"})
public class WarehouseRecordController {

    @Autowired
    private WarehouseRecordService warehouseRecordService;
    @Resource
    private WarehouseRecordCommService warehouseRecordCommService;

    @ApiOperation(value = "查询入库单列表", nickname = "query_in_warehouse_record_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/in_warehouse_record/queryInWarehouseRecordList", method = RequestMethod.POST)
    public Response<PageInfo<WarehouseRecordPageDTO>> queryInWarehouseRecordList(@ApiParam(name = "inRecord", value = "dto") @RequestBody WarehouseRecordPageDTO warehouseRecord) {
        try {
            PageInfo<WarehouseRecordPageDTO> personPageInfo = warehouseRecordService.queryInWarehouseRecordList(warehouseRecord);
            return Response.builderSuccess(personPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询出库单列表", nickname = "query_out_warehouse_record_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/out_warehouse_record/queryOutWarehouseRecordList", method = RequestMethod.POST)
    public Response queryOutWarehouseRecordList(@ApiParam(name = "OutRecord", value = "dto") @RequestBody WarehouseRecordPageDTO warehouseRecord) {
        try {
            PageInfo<WarehouseRecordPageDTO> personPageInfo = warehouseRecordService.queryOutWarehouseRecordList(warehouseRecord);
            return Response.builderSuccess(personPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "查询出入库单详情列表", nickname = "query_warehouse_record_detail_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/warehouse_record/queryWarehouseRecordDetailList", method = RequestMethod.GET)
    public Response<List<WarehouseRecordDetailDTO>> queryWarehouseRecordDetailList(@ApiParam(name = "warehouseId", value = "warehouseId") @RequestParam("warehouseRecordId") Long warehouseRecordId) {
        try {
            List<WarehouseRecordDetailDTO> list = warehouseRecordService.queryWarehouseRecordDetailList(warehouseRecordId);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "【定时器】分页查询待推送交易的单子", nickname = "queryNeedSyncTradeRecordByPage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryNeedSyncTradeRecordByPage", method = RequestMethod.POST)
    public Response<List<WarehouseRecordPageDTO>> queryNeedSyncTradeRecordByPage(@RequestParam Integer page, @RequestParam Integer maxResult) {
        try {
            List<WarehouseRecordPageDTO> res = warehouseRecordService.queryNeedSyncTradeRecordByPage(page, maxResult);
            return ResponseMsg.SUCCESS.buildMsg(res);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.EXCEPTION.buildMsg(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询待同步到派车系统的出库单", nickname = "query_init_warehouse_record_list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/queryNeedSyncTmsBWarehouseRecords", method = RequestMethod.GET)
    public Response<List<Long>> queryNeedSyncTmsBWarehouseRecords(@RequestParam("startPage") Integer startPage, @RequestParam("endPage") Integer endPage) {
        try {
            List<Long> list = warehouseRecordService.queryNeedSyncTmsBWarehouseRecords(startPage, endPage);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "处理出库单同步到派车系统", nickname = "handleDispatchCarWarehouseRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/handleDispatchCarWarehouseRecord", method = RequestMethod.POST)
    public Response<String> handleDispatchCarWarehouseRecord(@ApiParam(name = "id", value = "后置单id") @RequestParam Long id) {
        try {
            warehouseRecordService.handleDispatchCarWarehouseRecord(id);
            return Response.builderSuccess("");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "【定时器】处理待同步交易的单子", nickname = "processSyncTradeStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/processSyncTradeStatus", method = RequestMethod.POST)
    public Response processSyncTradeStatus(@RequestBody WarehouseRecordPageDTO warehouseRecord) {
        try {
            warehouseRecordService.processSyncTradeStatus(warehouseRecord);
            return Response.builderSuccess("success");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "【库存中心使用】通过后置单查询送达方编码", nickname = "queryRecordReceiveCodeByRecordCode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/queryRecordReceiveCodeByRecordCode", method = RequestMethod.GET)
    public Response<String> queryRecordReceiveCodeByRecordCode(@ApiParam(name = "recordCode", value = "后置单编码") @RequestParam("recordCode") String recordCode,
            @ApiParam(name = "type", value = "wms业务类型")@RequestParam("type") String type) {
        try {
            String receiveCode = warehouseRecordCommService.queryRecordReceiveCode(recordCode, type);
            return Response.builderSuccess(receiveCode);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "【库存中心使用】通过后置单查询前置单列表", nickname = "queryCommonFrontRecordInfoByRecordCode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/queryCommonFrontRecordInfoByRecordCode", method = RequestMethod.GET)
    public Response<List<CommonFrontRecordDTO>> queryCommonFrontRecordInfoByRecordCode(@ApiParam(name = "recordCode", value = "recordCode") @RequestParam String recordCode) {
        try {
            List<CommonFrontRecordDTO> result = warehouseRecordCommService.queryCommonFrontRecordInfoByRecordCode(recordCode);
            return Response.builderSuccess(result);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "通过入库单号查询出库单号：库存中心-直送场景使用", nickname = "queryOutWhRecordByInWhRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = Response.class)
    @RequestMapping(value = "/queryOutWhRecordByInWhRecord", method = RequestMethod.GET)
    public Response<List<String>> queryOutWhRecordByInWhRecord(@ApiParam(name = "recordCode", value = "recordCode") @RequestParam String recordCode) {
        try {
            List<String> outRecordCodeList = warehouseRecordService.queryOutWhRecordByInWhRecord(recordCode);
            return Response.builderSuccess(outRecordCodeList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}

