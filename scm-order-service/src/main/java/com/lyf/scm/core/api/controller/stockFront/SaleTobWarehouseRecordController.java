package com.lyf.scm.core.api.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordCondition;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordDTO;
import com.lyf.scm.core.service.stockFront.SaleTobWarehouseRecordService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RomeController
@RequestMapping("/order/v1/warehouse_sale_tob")
@Api(tags={"销售发货单tob管理"})
public class SaleTobWarehouseRecordController {
	@Resource
	private SaleTobWarehouseRecordService saleTobWarehouseRecordService;


	@ApiOperation(value = "查询发货单", nickname = "query_sale_warehouse_record", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Response<PageInfo<SaleTobWarehouseRecordDTO>> queryByCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody SaleTobWarehouseRecordCondition condition) {
		try {
			//在后台再做一次时间校验
			if (condition.getStartTime() == null || condition.getEndTime() == null) {
				throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":时间必填");
			}
			long max = 3600 * 24 * 30 * 1000L;
			if (condition.getEndTime().getTime() - condition.getStartTime().getTime() > max) {
				throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + ":时间跨度不能大于30天");
			}
			return Response.builderSuccess(saleTobWarehouseRecordService.queryWarehouseRecordList(condition));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}
	@ApiOperation(value = "获取已有所需前置单据门店信息", nickname = "getShopInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/getShopInfo", method = RequestMethod.GET)
	public Response<List<Map<String,String>>> getShopInfo() {
		try {
			return Response.builderSuccess(saleTobWarehouseRecordService.getShopInfo());
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}
	@ApiOperation(value = "根据发货单id查看详情", nickname = "getWarehouseSaleTobDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/getWarehouseSaleTobDetail", method = RequestMethod.POST)
	public Response<SaleTobWarehouseRecordDTO> getWarehouseSaleTobDetail(@RequestParam("warehouseRecordId") Long warehouseRecordId) {
		try {
			return Response.builderSuccess(saleTobWarehouseRecordService.getWarehouseSaleTobDetail(warehouseRecordId));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}
}
