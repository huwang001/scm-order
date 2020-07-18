package com.lyf.scm.core.api.controller.stockFront;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.service.stockFront.ShowTypeStatusService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取类型和状态
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/show")
@Api(tags = { "前置单类型和状态管理" })
public class ShowTypeStatusController {

	@Autowired
	private ShowTypeStatusService showTypeStatusService;

	@ApiOperation(value = "前置单状态列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/front_record/getFrontRecordStatusList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getFrontRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getFrontRecordStatusList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "前置单类型列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/front_record/getFrontRecordTypeList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getFrontRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getFrontRecordTypeList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "出入单状态列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getWarehouseRecordStatusList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getWarehouseRecordStatusList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "入库单页面状态列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getInWarehouseRecordStatusList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getInWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getInWarehouseRecordStatusList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "出库单页面状态列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getOutWarehouseRecordStatusList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getOutWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getOutWarehouseRecordStatusList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "查找数据库中已存在的所有非Z补货出库单的状态", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getReplenishOutWarehouseRecordStatusList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getReplenishOutWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getReplenishOutWarehouseRecordStatusList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "出入单类型列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getWarehouseRecordTypeList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getWarehouseRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getWarehouseRecordTypeList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "入库单页面类型列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getInWarehouseRecordTypeList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getInWarehouseRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getInWarehouseRecordTypeList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "出库单页面类型列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getOutWarehouseRecordTypeList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getOutWarehouseRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getOutWarehouseRecordTypeList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "加工单类型列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/front_record/getWarehouseAssembleRecordTypeList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getWarehouseAssembleRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getWarehouseAssembleRecordTypeList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "非Z补货出库单列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getReplenishOutWarehouseRecordList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getReplenishOutWarehouseRecordList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getReplenishOutWarehouseRecordList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有实仓流水库存类型（出库）", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/change_flow/getOutStockType", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getOutStockType() {
		try {
			Map<Integer, String> list = showTypeStatusService.getOutStockType();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有实仓流水库存类型(入库)", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/change_flow/getInStockType", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getInStockType() {
		try {
			Map<Integer, String> list = showTypeStatusService.getInStockType();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有实仓流水库存类型", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/change_flow/getAllStockType", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getAllStockType() {
		try {
			Map<Integer, String> list = showTypeStatusService.getAllStockType();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有库存类型枚举", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/real_warehouse_batch/getInventoryType", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getInventoryType() {
		try {
			Map<Integer, String> list = showTypeStatusService.getInventoryType();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有质检状态枚举", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/real_warehouse_batch/getQualityStatus", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getQualityStatus() {
		try {
			Map<Integer, String> list = showTypeStatusService.getQualityStatus();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取wms同步状态", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/warehouse_record/getWmsSyncStatusList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getWmsSyncStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusService.getWmsSyncStatusList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

}