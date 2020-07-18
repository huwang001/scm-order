package com.lyf.scm.admin.remote;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.order.OrderCreatePackDemandDTO;
import com.lyf.scm.admin.dto.order.OrderDetailRespDTO;
import com.lyf.scm.admin.dto.order.QueryOrderDTO;
import com.lyf.scm.admin.remote.dto.OrderRespDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * com.lyf.scm.admin.remote
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@FeignClient(value = "scm-order-service", url = "http://127.0.0.1:8082")
public interface OrderRemoteService {

    @RequestMapping(value = "/order/v1/order/getAllOrderStatusList", method = RequestMethod.GET)
    Response<Map<Integer, String>> getAllOrderStatusList();

    @PostMapping("/order/v1/order/pageOrder")
    Response<PageInfo<OrderRespDTO>> pageOrder(
            @RequestBody QueryOrderDTO queryOrderDTO,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/order/v1/order/queryOrder")
    Response<List<OrderRespDTO>> queryOrder(@RequestBody QueryOrderDTO queryOrderDTO);

    @PostMapping("/order/v1/order/pageOrderDetail")
    Response<PageInfo<OrderDetailRespDTO>> pageOrderDetail(
            @RequestParam("orderCode") String orderCode,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/order/v1/order/orderDetail")
    Response<List<OrderDetailRespDTO>> orderDetail(@RequestParam("orderCode") String orderCode);

    @GetMapping("/order/v1/order/queryOrder")
    Response<OrderRespDTO> queryOrder(@RequestParam("orderCode") String orderCode);

    @PostMapping("/order/v1/order/packCompleted")
    Response<Boolean> packCompleted(@RequestParam("orderCode") String orderCode, @RequestParam("userId") Long userId);

    @PostMapping("/order/v1/order/createDo")
    Response<Boolean> createDo(@RequestParam("orderCode") String orderCode, @RequestParam("userId") Long userId);

    /**
     * 强制生成DO
     * @param orderCode
     * @param userId
     * @return
     */
    @PostMapping("/order/v1/order/forceCreateDo")
    Response<Boolean> forceCreateDo(@RequestParam("orderCode") String orderCode, @RequestParam("userId") Long userId);

    @RequestMapping(value = "/order/v1/order/createPackDemand", method = RequestMethod.POST)
    Response createPackDemand(@RequestBody OrderCreatePackDemandDTO orderCreatePackDemandDTO);

}
