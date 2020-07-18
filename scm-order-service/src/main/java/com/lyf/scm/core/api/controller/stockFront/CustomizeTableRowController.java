package com.lyf.scm.core.api.controller.stockFront;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.stockFront.CustomizeTableRowDTO;
import com.lyf.scm.core.service.stockFront.CustomizeTableRowService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RomeController
@RequestMapping("/order/v1/customize_table_row")
@Api(tags={"用户自定义标题处理类"})
public class CustomizeTableRowController {
	
	@Resource
	private CustomizeTableRowService customizeTableRowService;

	@ApiOperation(value = "根据table_code和用户获取自定义数据", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/getDetailByTableCodeAndUserId", method = RequestMethod.GET)
	public Response<List<CustomizeTableRowDTO>> getWarehouseSaleTobDetail(@RequestParam("tableCode") String tableCode, @RequestParam("userId") Long userId) {
		try {
			return Response.builderSuccess(customizeTableRowService.getDetailByTableCodeAndUserId(tableCode,userId));
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}
	@ApiOperation(value = "根据CustomizeTableRowDTO对象集合更新某表的标题信息", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/updateDetailByDates", method = RequestMethod.POST)
	public Response updateDetailByDates(@RequestBody List<CustomizeTableRowDTO> CustomizeTableRowDTOs) {
		try {
			customizeTableRowService.updateDetailByDates(CustomizeTableRowDTOs);
			return Response.builderSuccess("");
		} catch (RomeException e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
		}
	}

}