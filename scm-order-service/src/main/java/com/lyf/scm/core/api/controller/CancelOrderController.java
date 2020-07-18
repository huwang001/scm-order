package com.lyf.scm.core.api.controller;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.service.order.CancelOrderService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Description: 取消预约单controller
 * <p>
 * @Author: wwh 2020/4/8
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1")
@Api(tags={"取消预约单接口管理"})
public class CancelOrderController {
	
	@Resource
	private CancelOrderService cancelOrderService;

	@ApiOperation(value = "根据销售单号取消预约单", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
	public Response cancelOrder(@ApiParam(name = "saleCode", value = "销售单号") @RequestParam(name = "saleCode") String saleCode) {
	    try {
	    	log.info("根据销售单号取消预约单 <<< {}", saleCode);
	    	cancelOrderService.cancelOrder(saleCode);
	        return Response.builderSuccess(null);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "根据销售单号回滚预约单", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/rollbackOrder", method = RequestMethod.POST)
	public Response rollbackOrder(@ApiParam(name = "saleCode", value = "销售单号") @RequestParam(name = "saleCode") String saleCode) {
	    try {
	    	log.info("根据销售单号回滚预约单 <<< {}", saleCode);
	    	cancelOrderService.cancelOrder(saleCode);
	        return Response.builderSuccess(null);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
}