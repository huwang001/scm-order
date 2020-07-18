package com.lyf.scm.admin.controller.stockFront;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lyf.scm.admin.remote.stockFront.facade.ShowTypeStatusFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 状态和类型集合
 */
@Slf4j
@RestController
@RequestMapping("/order/show")
public class ShowTypeStatusController {

	@Autowired
	private ShowTypeStatusFacade showTypeStatusFacade;

	@ApiOperation(value = "前置单状态列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/queryFrontRecordStatus/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryFrontRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryFrontRecordStatusList();
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
	@RequestMapping(value = "/queryFrontRecordType/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryFrontRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryFrontRecordTypeList();
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
	@RequestMapping(value = "/queryWarehouseRecordStatus/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryWarehouseRecordStatusList();
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
	@RequestMapping(value = "/queryInWarehouseRecordStatus/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryInWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryInWarehouseRecordStatusList();
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
	@RequestMapping(value = "/queryOutWarehouseRecordStatus/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryOutWarehouseRecordStatusList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryOutWarehouseRecordStatusList();
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
			Map<Integer, String> list = showTypeStatusFacade.getReplenishOutWarehouseRecordStatusList();
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
	@RequestMapping(value = "/queryWarehouseRecordType/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryWarehouseRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryWarehouseRecordTypeList();
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
	@RequestMapping(value = "/queryInWarehouseRecordType/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryInWarehouseRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryInWarehouseRecordTypeList();
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
	@RequestMapping(value = "/queryOutWarehouseRecordType/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryOutWarehouseRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryOutWarehouseRecordTypeList();
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
	@RequestMapping(value = "/queryWarehouseAssembleRecordType/list", method = RequestMethod.POST)
	public Response<Map<Integer, String>> queryWarehouseAssembleRecordTypeList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.queryWarehouseAssembleRecordTypeList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "非Z补货出库单列表", nickname = "getReplenishOutWarehouseRecordList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success", response = Map.class)
	@RequestMapping(value = "/warehouse_record/getReplenishOutWarehouseRecordList", method = RequestMethod.POST)
	public Response<Map<Integer, String>> getReplenishOutWarehouseRecordList() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.getReplenishOutWarehouseRecordList();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有实仓流水库存类型（出库）", nickname = "getOutStockType", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success", response = Map.class)
	@RequestMapping(value = "/change_flow/getOutStockType", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getOutStockType() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.getOutStockType();
			return Response.builderSuccess(list);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "获取所有实仓流水库存类型(入库)", nickname = "getInStockType", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success", response = Map.class)
	@RequestMapping(value = "/change_flow/getInStockType", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getInStockType() {
		try {
			Map<Integer, String> list = showTypeStatusFacade.getInStockType();
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
			Map<Integer, String> list = showTypeStatusFacade.getAllStockType();
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
			Map<Integer, String> list = showTypeStatusFacade.getInventoryType();
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
			Map<Integer, String> list = showTypeStatusFacade.getQualityStatus();
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
			Map<Integer, String> list = showTypeStatusFacade.getWmsSyncStatusList();
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
