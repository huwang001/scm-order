package com.lyf.scm.core.api.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnNoticeDTO;
import com.lyf.scm.core.service.orderReturn.OrderReturnService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 退货单controller
 * <p>
 * @Author: wwh 2020/4/8
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/orderReturn")
@Api(tags={"退货单接口管理"})
public class OrderReturnController {

	@Resource
	private OrderReturnService orderReturnService;
	
	@ApiOperation(value = "根据条件查询退货单列表-分页", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/queryOrderReturnPageByCondition", method = RequestMethod.POST)
	public Response<PageInfo<OrderReturnDTO>> queryOrderReturnPageByCondition(@ApiParam(name = "OrderReturnDTO", value = "退货单查询对象") @RequestBody @Valid OrderReturnDTO orderReturnDTO) {
	    try {
	        PageInfo<OrderReturnDTO> pageInfo = orderReturnService.queryOrderReturnPageByCondition(orderReturnDTO);
	        return Response.builderSuccess(pageInfo);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "根据售后单号查询退货单详情列表-分页", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/queryOrderReturnDetailPageByAfterSaleCode", method = RequestMethod.POST)
	public Response<OrderReturnDTO> queryOrderReturnDetailPageByAfterSaleCode(@ApiParam(name = "afterSaleCode", value = "售后单号") @RequestParam(name = "afterSaleCode") String afterSaleCode,@ApiParam(name = "pageNum", value = "页码") @RequestParam(name = "pageNum") Integer pageNum,
    		@ApiParam(name = "pageSize", value = "每页记录数") @RequestParam(name = "pageSize") Integer pageSize) {
	    try {
	    	OrderReturnDTO orderReturnDTO = orderReturnService.queryOrderReturnDetailPageByAfterSaleCode(afterSaleCode, pageNum, pageSize);
	        return Response.builderSuccess(orderReturnDTO);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "根据售后单号查询退货单（包含退货单详情）", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/queryOrderReturnWithDetailByAfterSaleCode/{afterSaleCode}", method = RequestMethod.GET)
	public Response<OrderReturnDTO> queryOrderReturnWithDetailByAfterSaleCode(@ApiParam(name = "afterSaleCode", value = "售后单号") @PathVariable(name = "afterSaleCode") String afterSaleCode) {
	    try {
	    	OrderReturnDTO orderReturnDTO = orderReturnService.queryOrderReturnWithDetailByAfterSaleCode(afterSaleCode);
	        return Response.builderSuccess(orderReturnDTO);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "根据售后单号查询退货单详情列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/queryOrderReturnDetailByAfterSaleCode/{afterSaleCode}", method = RequestMethod.GET)
	public Response<List<OrderReturnDetailDTO>> queryOrderReturnDetailByAfterSaleCode(@ApiParam(name = "afterSaleCode", value = "售后单号") @PathVariable(name = "afterSaleCode") String afterSaleCode) {
	    try {
	    	List<OrderReturnDetailDTO> orderReturnDetailDTOList = orderReturnService.queryOrderReturnDetailByAfterSaleCode(afterSaleCode);
	        return Response.builderSuccess(orderReturnDetailDTOList);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "接收交易中心退货单", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/receiveReturn", method = RequestMethod.POST)
	public Response receiveReturn(@ApiParam(name = "ReturnDTO", value = "交易中心退货单对象") @RequestBody @Valid ReturnDTO returnDTO) {
	    try {
	    	log.info("接收交易中心退货单 <<< {}", JSON.toJSONString(returnDTO));
	        orderReturnService.receiveReturn(returnDTO);
	        return Response.builderSuccess(null);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "接收库存中心退货入库通知", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/receiveReturnNotice", method = RequestMethod.POST)
	public Response receiveReturnNotice(@ApiParam(name = "ReturnNoticeDTO", value = "库存中心退货入库通知对象") @RequestBody @Valid ReturnNoticeDTO returnNoticeDTO) {
	    try {
	    	log.info("接收库存中心退货入库通知 <<< {}", JSON.toJSONString(returnNoticeDTO));
	        orderReturnService.receiveReturnNotice(returnNoticeDTO);
	        return Response.builderSuccess(null);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "查询待推送给交易中心的退货单列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/queryOrderReturnToTrade", method = RequestMethod.GET)
	public Response<List<OrderReturnDTO>> queryOrderReturnToTrade() {
	    try {
	    	List<OrderReturnDTO> orderReturnDTOList = orderReturnService.queryOrderReturnToTrade();
	        return Response.builderSuccess(orderReturnDTOList);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}
	
	@ApiOperation(value = "处理待推送给交易中心的退货单", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/handleOrderReturnToTrade", method = RequestMethod.POST)
	public Response handleOrderReturnToTrade(@ApiParam(name = "afterSaleCode", value = "售后单号") @RequestParam(name = "afterSaleCode") String afterSaleCode) {
	    try {
	    	log.info("处理待推送给交易中心的退货单 <<< {}", afterSaleCode);
	    	orderReturnService.handleOrderReturnToTrade(afterSaleCode);
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