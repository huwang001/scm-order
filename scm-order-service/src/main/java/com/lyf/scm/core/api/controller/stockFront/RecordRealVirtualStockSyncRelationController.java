package com.lyf.scm.core.api.controller.stockFront;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.core.service.stockFront.RecordRealVirtualStockSyncRelationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 类WhAllocationController的实现描述:仓库调拨
 *
 * @author sunyj 2019/5/13 10:20
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/RecordSyncRelation")
@Api(tags = { "单据虚仓实仓同步比率" })
public class RecordRealVirtualStockSyncRelationController {

	@Autowired
	private RecordRealVirtualStockSyncRelationService recordRealVirtualStockSyncRelationService;

	@ApiOperation(value = "根据单据code查询单据的sku级别的实仓虚仓的配比关系", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/queryRelationMapByRecordCode/{recordCode}", method = RequestMethod.GET)
	public Response<Map<String, RecordRealVirtualStockSyncRelationDTO>> queryRelationMapByRecordCode(
			@ApiParam(name = "recordCode", value = "单据编号") @PathVariable("recordCode") String recordCode) {
		try {
			return Response
					.builderSuccess(recordRealVirtualStockSyncRelationService.queryRelationMapByRecordCode(recordCode));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "查询指定实仓的比例", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/queryRelationMapByRecordCodeAndRwId", method = RequestMethod.POST)
	public Response<Map<String, RecordRealVirtualStockSyncRelationDTO>> queryRelationMapByRecordCode(
			@ApiParam(name = "recordCode", value = "单据编号") @RequestParam("recordCode") String recordCode,
			@ApiParam(name = "rwId", value = "实仓ID") @RequestParam("recordCode") Long rwId) {
		try {
			return Response.builderSuccess(
					recordRealVirtualStockSyncRelationService.queryRelationMapByRecordCode(recordCode, rwId));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "根据单据编号查询单据同步比率列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/queryRelationByRecordCode/{recordCode}", method = RequestMethod.GET)
	public Response<List<RecordRealVirtualStockSyncRelationDTO>> queryRelationByRecordCode(
			@ApiParam(name = "recordCode", value = "单据编号") @PathVariable("recordCode") String recordCode) {
		try {
			return Response.builderSuccess(
					recordRealVirtualStockSyncRelationService.queryRelationListByRecordCode(recordCode));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "根据单据编号、实仓ID查询单据同步比率列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/queryRelationByRecordCodeAndRwId", method = RequestMethod.POST)
	public Response<List<RecordRealVirtualStockSyncRelationDTO>> queryRelationByRecordCodeAndRwId(
			@ApiParam(name = "recordCode", value = "单据编号") @RequestParam("recordCode") String recordCode,
			@ApiParam(name = "rwId", value = "实仓ID") @RequestParam("recordCode") Long rwId) {
		try {
			return Response.builderSuccess(
					recordRealVirtualStockSyncRelationService.queryRelationListByRecordCode(recordCode, rwId));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

	@ApiOperation(value = "配置单据级别的sku实仓虚仓分配比例", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/allotConfig", method = RequestMethod.POST)
	public Response allotConfig(
			@ApiParam(name = "config", value = "单据级别的sku实仓虚仓分配比例") @RequestBody List<RecordRealVirtualStockSyncRelationDTO> config) {
		try {
			recordRealVirtualStockSyncRelationService.insertRecordRealVirtualStockRelation(config);
			return Response.builderSuccess(null);
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

}