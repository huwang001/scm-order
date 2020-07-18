package com.lyf.scm.admin.remote.stockFront;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rome.arch.core.clientobject.Response;

/**
 * 类FrontRecordRemoteService的实现描述：
 *
 * @author sunyj 2019/4/18 21:08
 */
@FeignClient(value = "scm-order-service")
public interface ShowTypeStatusRemoteService {

	@RequestMapping(value = "/order/v1/show/front_record/getFrontRecordStatusList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryFrontRecordStatusList();

	@RequestMapping(value = "/order/v1/show/front_record/getFrontRecordTypeList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryFrontRecordTypeList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getWarehouseRecordStatusList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryWarehouseRecordStatusList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getInWarehouseRecordStatusList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryInWarehouseRecordStatusList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getOutWarehouseRecordStatusList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryOutWarehouseRecordStatusList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getReplenishOutWarehouseRecordStatusList", method = RequestMethod.POST)
	Response<Map<Integer, String>> getReplenishOutWarehouseRecordStatusList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getWarehouseRecordTypeList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryWarehouseRecordTypeList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getInWarehouseRecordTypeList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryInWarehouseRecordTypeList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getOutWarehouseRecordTypeList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryOutWarehouseRecordTypeList();

	@RequestMapping(value = "/order/v1/show/front_record/getWarehouseAssembleRecordTypeList", method = RequestMethod.POST)
	Response<Map<Integer, String>> queryWarehouseAssembleRecordTypeList();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getReplenishOutWarehouseRecordList", method = RequestMethod.POST)
	Response<Map<Integer, String>> getReplenishOutWarehouseRecordList();

	@RequestMapping(value = "/order/v1/show/change_flow/getOutStockType", method = RequestMethod.GET)
	Response<Map<Integer, String>> getOutStockType();

	@RequestMapping(value = "/order/v1/show/change_flow/getInStockType", method = RequestMethod.GET)
	Response<Map<Integer, String>> getInStockType();

	@RequestMapping(value = "/order/v1/show/change_flow/getAllStockType", method = RequestMethod.GET)
	Response<Map<Integer, String>> getAllStockType();

	@RequestMapping(value = "/order/v1/show//real_warehouse_batch/getInventoryType", method = RequestMethod.GET)
	Response<Map<Integer, String>> getInventoryType();

	@RequestMapping(value = "/order/v1/show//real_warehouse_batch/getQualityStatus", method = RequestMethod.GET)
	Response<Map<Integer, String>> getQualityStatus();

	@RequestMapping(value = "/order/v1/show/warehouse_record/getWmsSyncStatusList", method = RequestMethod.POST)
	Response<Map<Integer, String>> getWmsSyncStatusList();

}