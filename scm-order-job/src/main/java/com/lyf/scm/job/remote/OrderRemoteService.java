package com.lyf.scm.job.remote;

import com.lyf.scm.job.remote.dto.OrderDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "scm-order-service")
public interface OrderRemoteService {

    @RequestMapping(value = "/order/v1/order/lockInventory", method = RequestMethod.POST)
    Response<Boolean> lockInventory(@RequestParam("recordCode") String recordCode);

    @RequestMapping(value = "/order/v1/order/notifyTradeAfterLocked", method = RequestMethod.POST)
    Response<Boolean> notifyTradeAfterLocked(@RequestBody OrderDTO orderDTO);

    @RequestMapping(value = "/order/v1/order/notifyTradeDeliveryStatus", method = RequestMethod.POST)
    Response<Boolean> notifyTradeDeliveryStatus(@RequestBody OrderDTO orderDTO);

    @RequestMapping(value = "/order/v1/order/findOrderBySyncTradeStatus", method = RequestMethod.POST)
    Response<List<OrderDTO>> findOrderBySyncTradeStatus(@RequestParam("syncTradeStatus") Integer syncTradeStatus);

    @RequestMapping(value = "/order/v1/order/queryWaitLockOrder", method = RequestMethod.GET)
	Response<List<String>> queryWaitLockOrder();
    
}
