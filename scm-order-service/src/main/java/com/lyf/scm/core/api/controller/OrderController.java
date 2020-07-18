package com.lyf.scm.core.api.controller;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.CommonConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.OrderStatusEnum;
import com.lyf.scm.core.api.dto.order.*;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import com.lyf.scm.core.service.order.OrderService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约单接口
 *
 * @author zhangxu
 * @date 2020/4/8
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/order")
@Api(tags = {"预约单接口"})
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation(value = "接收交易中心预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/receiveTradeOrder")
    public Response<Boolean> receiveTradeOrder(
            @ApiParam(name = "orderDTO", value = "预约单对象") @RequestBody @Valid TradeOrderDTO tradeOrderDTO) {
        try {
        	//needPackage是否需要包装0否 1是
        	if(Integer.valueOf(1).equals(tradeOrderDTO.getNeedPackage())) {
        		if(tradeOrderDTO.getPackageNum() == null || Integer.valueOf(0).equals(tradeOrderDTO.getPackageNum())) {
        			throw new RomeException(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC + "：包装份数[packageNum]必填");
        		}
        	}
            orderService.receiveTradeOrder(tradeOrderDTO);
            return Response.builderSuccess(Boolean.TRUE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "接收交易中心可以发货通知", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/receiveTradeDeliveryNotice")
    public Response<Boolean> receiveTradeDeliveryNotice(
            @ApiParam(name = "saleCode", value = "销售单号") @RequestParam(name = "saleCode") String saleCode) {
        try {
            orderService.receiveTradeDeliveryNotice(saleCode);
            return Response.builderSuccess(Boolean.TRUE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "分页查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/pageOrder")
    public Response<PageInfo<OrderRespDTO>> pageOrder(
            @ApiParam(name = "queryOrderDTO", value = "预约单查询条件对象") @RequestBody @Valid QueryOrderDTO queryOrderDTO,
            @ApiParam(name = "pageNum", value = "当前页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam("pageSize") Integer pageSize) {
        try {
            PageInfo<OrderRespDTO> pageInfo = orderService.pageOrder(queryOrderDTO, pageNum, pageSize);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/queryOrder")
    public Response<List<OrderRespDTO>> queryOrder(
            @ApiParam(name = "queryOrderDTO", value = "预约单查询条件对象") @RequestBody @Valid QueryOrderDTO queryOrderDTO) {
        try {
            return Response.builderSuccess(orderService.findOrder(queryOrderDTO));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "分页查询预约单明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/pageOrderDetail")
    public Response<PageInfo<OrderDetailRespDTO>> pageOrderDetail(
            @ApiParam(name = "orderCode", value = "预约单号") @RequestParam("orderCode") String orderCode,
            @ApiParam(name = "pageNum", value = "当前页码") @RequestParam("pageNum") Integer pageNum,
            @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam("pageSize") Integer pageSize) {
        try {
            PageInfo<OrderDetailRespDTO> pageInfo = orderService.pageOrderDetail(orderCode, pageNum, pageSize);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询预约单明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/orderDetail")
    public Response<List<OrderDetailRespDTO>> orderDetail(@ApiParam(name = "orderCode", value = "预约单号") @RequestParam("orderCode") String orderCode) {
        try {
            List<OrderDetailRespDTO> list = orderService.queryOrderDetailByOrderCode(orderCode);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "加工完成操作", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/packCompleted")
    public Response<Boolean> completeProcess(
            @ApiParam(name = "orderCode", value = "预约单号") @RequestParam("orderCode") String orderCode,
            @ApiParam(name = "userId", value = "操作用户") @RequestParam("userId") Long userId) {
        try {
            orderService.completeProcess(orderCode, userId);
            return Response.builderSuccess(Boolean.TRUE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "锁库存", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/lockInventory")
    public Response<Boolean> lockInventory(@RequestParam("recordCode") String recordCode) {
        try {
        	log.info("预约单锁虚仓库存 <<< {}", recordCode);
            orderService.lockInventory(recordCode);
            return Response.builderSuccess(Boolean.TRUE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "完全锁定后通知交易中心", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/notifyTradeAfterLocked")
    public Response<Boolean> notifyTradeAfterLocked(@RequestBody OrderDTO orderDTO) {
        try {
            int row = orderService.notifyTradeAfterLocked(orderDTO);
            return Response.builderSuccess(row == 1 ? Boolean.TRUE : Boolean.FALSE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "通知交易中心发货状态", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/notifyTradeDeliveryStatus")
    public Response<Boolean> notifyTradeDeliveryStatus(@RequestBody OrderDTO orderDTO) {
        try {
            int row = orderService.notifyTradeDeliveryStatus(orderDTO);
            return Response.builderSuccess(row == 1 ? Boolean.TRUE : Boolean.FALSE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据销售单号查询预约单锁定状态", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @GetMapping("/queryOrderDetailLockStatus")
    public Response<List<OrderDetailLockStatusDTO>> queryOrderDetailLockStatus(
            @ApiParam(name = "saleCode", value = "销售单号") @RequestParam("saleCode") String saleCode) {
        try {
            return Response.builderSuccess(orderService.queryOrderDetailLockStatus(saleCode));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据销售单号查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @GetMapping("/queryOrderBySaleCode")
    public Response<List<OrderRespDTO>> queryOrderBySaleCode(
            @ApiParam(name = "saleCode", value = "销售单号") @RequestParam("saleCode") String saleCode) {
        try {
        	OrderRespDTO orderRespDTO = orderService.findBySaleCode(saleCode);
            return Response.builderSuccess(orderRespDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据销售单号查询预约单及明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @GetMapping("/queryOrderAndOrderDetailBySaleCode")
    public Response<List<OrderRespDTO>> queryOrderAndOrderDetailBySaleCode(
            @ApiParam(name = "saleCode", value = "销售单号") @RequestParam("saleCode") String saleCode) {
        try {
        	OrderRespDTO orderRespDTO = orderService.queryOrderAndOrderDetailBySaleCode(saleCode);
            return Response.builderSuccess(orderRespDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据预约单号查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @GetMapping("/queryOrder")
    public Response<OrderRespDTO> queryOrderByOrderCode(
            @ApiParam(name = "orderCode", value = "预约单号") @RequestParam("orderCode") String orderCode) {
        try {
            return Response.builderSuccess(orderService.findOrder(orderCode));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "获取预约单所有单据状态", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @RequestMapping(value = "/getAllOrderStatusList", method = RequestMethod.GET)
    public Response<Map<Integer, String>> getAllOrderStatusList() {
        try {
            Map<Integer, String> map = OrderStatusEnum.getAllOrderStatusList();
            return Response.builderSuccess(map);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "创建DO出库单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/createDo")
    public Response<Boolean> createDo(
            @ApiParam(name = "orderCode", value = "预约单号") @RequestParam("orderCode") String orderCode,
            @ApiParam(name = "userId", value = "操作用户") @RequestParam("userId") Long userId) {
        try {
            orderService.createDo(orderCode, userId);
            return Response.builderSuccess(Boolean.TRUE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "强制生成DO出库单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/forceCreateDo")
    public Response<Boolean> forceCreateDo(
            @ApiParam(name = "orderCode", value = "预约单号") @RequestParam("orderCode") String orderCode,
            @ApiParam(name = "userId", value = "操作用户") @RequestParam("userId") Long userId) {
        try {
            orderService.forceCreateDo(orderCode, userId);
            return Response.builderSuccess(Boolean.TRUE);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据同步交易状态查询预约单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = CommonConstants.SUCCESS)
    @PostMapping("/findOrderBySyncTradeStatus")
    public Response<List<OrderDTO>> findOrderBySyncTradeStatus(@RequestParam("syncTradeStatus") Integer syncTradeStatus) {
        // 避免数据量过大查询效率低下，默认处理一个月内的数据
        Date monthAgo = DateUtil.offsetMonth(new Date(), -1).toJdkDate();
        try {
            List<OrderDTO> orderDTOList = orderService.findBySyncTradeStatus(syncTradeStatus, monthAgo);
            return Response.builderSuccess(orderDTOList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询待锁定的预约单列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/queryWaitLockOrder", method = RequestMethod.GET)
	Response<List<String>> queryWaitLockOrder() {
        try {
            List<String> recordCodes = orderService.queryWaitLockOrder();
            return Response.builderSuccess(recordCodes);
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
            orderService.createPackDemand(orderCreatePackDemandDTO);
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
