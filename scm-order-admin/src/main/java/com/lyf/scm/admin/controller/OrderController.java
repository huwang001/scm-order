package com.lyf.scm.admin.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.common.excel.ExcelUtil;
import com.lyf.scm.admin.dto.OrderDetailExcelDTO;
import com.lyf.scm.admin.dto.OrderExcelDTO;
import com.lyf.scm.admin.dto.order.OrderCreatePackDemandDTO;
import com.lyf.scm.admin.dto.order.OrderDetailRespDTO;
import com.lyf.scm.admin.dto.order.OrderStatusDTO;
import com.lyf.scm.admin.dto.order.QueryOrderDTO;
import com.lyf.scm.admin.remote.OrderRemoteService;
import com.lyf.scm.admin.remote.dto.OrderRespDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.facade.OrderFacade;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.common.enums.OrderTypeEnum;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import com.rome.user.common.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * com.lyf.scm.admin.controller
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/order")
@Api(tags = {"预约单"})
public class OrderController {

	@Resource
    private OrderFacade orderFacade;

    @Resource
    private OrderRemoteService orderRemoteService;



    @ApiOperation(value = "分页查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/pageOrder")
    public Response<PageInfo<OrderRespDTO>> pageOrder(@ApiParam(name = "QueryOrderDTO", value = "预约单查询条件对象")
                                                 @RequestBody @Valid QueryOrderDTO queryOrderDTO, HttpServletRequest request) {
        try {
            Response<PageInfo<OrderRespDTO>> result = orderRemoteService.pageOrder(queryOrderDTO,queryOrderDTO.getPageNum(),queryOrderDTO.getPageSize());
            List<OrderRespDTO>  list = result.getData().getList();
            for(OrderRespDTO orderRespDTO :list){
                orderRespDTO.setCreateTimeStr(DateUtil.format(orderRespDTO.getCreateTime(),DatePattern.NORM_DATETIME_PATTERN));
                orderRespDTO.setExpectDateStr(DateUtil.format(orderRespDTO.getExpectDate(),DatePattern.NORM_DATE_PATTERN));
            }
//            request.getSession().setAttribute(ORDERRESPDTOLIST,JSON.toJSONString(list));
            return result;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


    @ApiOperation(value = "根据预约单号查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/findOrderByOrderCode/{orderCode}", method = RequestMethod.GET)
    public Response<OrderRespDTO> findOrderByOrderCode(@ApiParam(name = "orderCode", value = "预约单编号")
                                                                @PathVariable String orderCode, HttpServletRequest request) {
        try {
            Response<OrderRespDTO> resultOrder =  orderRemoteService.queryOrder(orderCode);
            resultOrder.getData().setExpectDateStr(DateUtil.format(resultOrder.getData().getExpectDate(),DatePattern.NORM_DATE_PATTERN));
            return resultOrder;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }



    @ApiOperation(value = "获取预约单状态列表", httpMethod = "POST", notes = "获取预约单状态列表")
    @RequestMapping(value = "/queryOrderStatusList", method = RequestMethod.POST)
    public Response<Map<Object, Object>> queryOrderStatusList (){

        List<OrderStatusDTO> list = new CopyOnWriteArrayList<>();
       for(OrderStatusEnum orderStatusEnum: OrderStatusEnum.values()){
           OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
           orderStatusDTO.setOrderStatusCode(orderStatusEnum.getStatus());
           orderStatusDTO.setOrderStatusName(orderStatusEnum.getDesc());
           list.add(orderStatusDTO);
       }
        return Response.builderSuccess(list);
    }






    @ApiOperation(value = "预约单导出", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportOrder(HttpServletResponse response, @RequestParam(required = false) String custom,
                            @RequestParam(required = false) String endTime , @RequestParam(required = false) String orderCode, @RequestParam(required = false) Integer orderStatus
            , @RequestParam(required = false) String saleCode, @RequestParam(required = false) String startTime) throws Exception {
        // 定义数据格式
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        log.error("endTime="+endTime);
        QueryOrderDTO queryOrderDTO = new QueryOrderDTO();
        queryOrderDTO.setCustom(custom);
        queryOrderDTO.setEndTime(DateUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN));
        queryOrderDTO.setOrderCode(orderCode);
        queryOrderDTO.setOrderStatus(orderStatus);
        queryOrderDTO.setSaleCode(saleCode);
        queryOrderDTO.setStartTime(DateUtil.parse(startTime,DatePattern.NORM_DATETIME_PATTERN));

        Response<List<OrderRespDTO>> result = orderRemoteService.queryOrder(queryOrderDTO);
        if (null == result||!"0".equals(result.getCode())) {
            log.warn("未查询到预约单：{}", JSON.toJSONString(queryOrderDTO));
            throw new RomeException("未查询到预约单");
        }
        List<OrderRespDTO> list = result.getData();
        List<OrderExcelDTO> orderExcelDTOS = list.stream().map(o -> {
            OrderExcelDTO orderExcelDTO = new OrderExcelDTO();
            BeanUtils.copyProperties(o, orderExcelDTO);
            orderExcelDTO.setOrderStatusStr(OrderStatusEnum.getDescByStatus(o.getOrderStatus()).getDesc());
            orderExcelDTO.setOrderTypeStr(OrderTypeEnum.getDescByStatus(o.getOrderType()).getDesc());
            return orderExcelDTO;
        }).collect(Collectors.toList());
        ExcelUtil.writeExcel(response, orderExcelDTOS, "预约单", "sheet1", OrderExcelDTO.class);
    }


    @ApiOperation(value = "预约单明细导出", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/exportOrderDetail", method = RequestMethod.GET)
    public void exportOrderDetail(HttpServletResponse response, @RequestParam(required = false) String custom,
                            @RequestParam(required = false) String endTime , @RequestParam(required = false) String orderCode, @RequestParam(required = false) Integer orderStatus
            , @RequestParam(required = false) String saleCode, @RequestParam(required = false) String startTime) throws Exception {
        // 定义数据格式
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        QueryOrderDTO queryOrderDTO = new QueryOrderDTO();
        queryOrderDTO.setCustom(custom);
        queryOrderDTO.setEndTime(DateUtil.parse(endTime, DatePattern.NORM_DATETIME_PATTERN));
        queryOrderDTO.setOrderCode(orderCode);
        queryOrderDTO.setOrderStatus(orderStatus);
        queryOrderDTO.setSaleCode(saleCode);
        queryOrderDTO.setStartTime(DateUtil.parse(startTime,DatePattern.NORM_DATETIME_PATTERN));

        Response<List<OrderRespDTO>> result = orderRemoteService.queryOrder(queryOrderDTO);
        if (null == result||!"0".equals(result.getCode())) {
            log.warn("未查询到预约单：{}", JSON.toJSONString(queryOrderDTO));
            throw new RomeException("未查询到预约单");
        }
        List<OrderRespDTO> list = result.getData();
        List<OrderDetailRespDTO> orderDetailRespDTOList = new CopyOnWriteArrayList<>();
        for(OrderRespDTO orderRespDTO :list){
            Response<List<OrderDetailRespDTO>> resultOrderDetail = orderRemoteService.orderDetail(orderRespDTO.getOrderCode());
            if(resultOrderDetail.getData()!=null) {
                List<OrderDetailRespDTO> orderDetailRespList = resultOrderDetail.getData();
               for(OrderDetailRespDTO orderDetailRespDTO :orderDetailRespList){
                   orderDetailRespDTO.setAllotCode(orderRespDTO.getAllotCode());
                   orderDetailRespDTO.setDoCode(orderRespDTO.getDoCode());
                   orderDetailRespDTO.setCustomName(orderRespDTO.getCustomName());
                   orderDetailRespDTO.setOrderType(orderRespDTO.getOrderType());
                   orderDetailRespDTO.setExpectDate(orderRespDTO.getExpectDate());
                   orderDetailRespDTO.setOrderStatus(orderRespDTO.getOrderStatus());
                   orderDetailRespDTO.setDoFactoryName(orderRespDTO.getDoFactoryName());
                   orderDetailRespDTO.setCreateTimeStr(DateUtil.format(orderDetailRespDTO.getCreateTime(),DatePattern.NORM_DATETIME_PATTERN));
                   orderDetailRespDTO.setExpectDateStr(DateUtil.format(orderDetailRespDTO.getExpectDate(),DatePattern.NORM_DATE_PATTERN));
               }
                orderDetailRespDTOList.addAll(orderDetailRespList);
            }
        }

        List<OrderDetailExcelDTO> orderDetailExcelDTOS = orderDetailRespDTOList.stream().map(o -> {
            OrderDetailExcelDTO orderDetailExcelDTO = new OrderDetailExcelDTO();
            BeanUtils.copyProperties(o, orderDetailExcelDTO);
            orderDetailExcelDTO.setDeliveryQty(orderDetailExcelDTO.getRequireQty());
            orderDetailExcelDTO.setOrderStatusStr(OrderStatusEnum.getDescByStatus(o.getOrderStatus()).getDesc());
            orderDetailExcelDTO.setOrderTypeStr(OrderTypeEnum.getDescByStatus(o.getOrderType()).getDesc());

            return orderDetailExcelDTO;
        }).collect(Collectors.toList());
        ExcelUtil.writeExcel(response, orderDetailExcelDTOS, "预约详情单", "sheet1", OrderDetailExcelDTO.class);
    }



    @ApiOperation(value = "预约单详情查询", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryOrderDetail/{orderCode}", method = RequestMethod.GET)
    public Response<PageInfo<OrderDetailRespDTO>> queryOrderDetail(@ApiParam(name = "orderCode", value = "预约单编号")
                                                 @PathVariable String orderCode,
                                                 @ApiParam(name = "pageNum", value = "当前页")
                                                 @RequestParam("pageNum") Integer pageNum,
                                                 @ApiParam(name = "pageSize", value = "分页大小")
                                                 @RequestParam("pageSize") Integer pageSize) {
        try {
            Response<PageInfo<OrderDetailRespDTO>> result = orderRemoteService.pageOrderDetail(orderCode,pageNum,pageSize);

            return  result;
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }






    @ApiOperation(value = "加工完成", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/processCompleted")
    public Response processCompleted(@RequestBody  QueryOrderDTO queryOrderDTO) {
        String orderCode = queryOrderDTO.getOrderCode();
        Long userId = UserContext.getUserId();
        Response<Boolean> result = orderRemoteService.packCompleted(orderCode, userId);
        return result;
    }

    @ApiOperation(value = "生成出库单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/createStockOut")
    public Response createStockOut(@RequestBody  QueryOrderDTO queryOrderDTO) {
        Long userId = UserContext.getUserId();
        Response<Boolean> result = orderRemoteService.createDo(queryOrderDTO.getOrderCode(), userId);
        return result;
    }

    @ApiOperation(value = "获取预约单所有单据状态", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponse(code = 200, message = "success")
	@RequestMapping(value = "/getAllOrderStatusList", method = RequestMethod.GET)
	public Response<Map<Integer, String>> getAllOrderStatusList() {
	    try {
	    	Map<Integer, String> map = orderFacade.getAllOrderStatusList();
	        return Response.builderSuccess(map);
	    } catch (RomeException e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(e.getCode(),e.getMessage());
	    } catch (Exception e) {
	        log.error(e.getMessage(), e);
	        return Response.builderFail(ResCode.ORDER_ERROR_1003,ResCode.ORDER_ERROR_1003_DESC);
	    }
	}

    @ApiOperation(value = "预约单界面强制创建DO功能", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping(value = "/forceCreateDo")
    public Response<Boolean> forceCreateDo(@RequestParam("orderCode") String orderCode) {
        try {
            Long userId = UserContext.getUserId();
            return orderFacade.forceCreateDo(orderCode, userId);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "预约单页面创建需求单", nickname = "createPackDemand", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/createPackDemand", method = RequestMethod.POST)
    public Response createPackDemand(@RequestBody OrderCreatePackDemandDTO orderCreatePackDemandDTO) {
        try {
            Long userId = UserContext.getUserId();
            orderCreatePackDemandDTO.setUserId(userId);
            orderFacade.createPackDemand(orderCreatePackDemandDTO);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


}
