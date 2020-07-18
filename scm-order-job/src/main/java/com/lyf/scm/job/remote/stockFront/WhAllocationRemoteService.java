package com.lyf.scm.job.remote.stockFront;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.job.api.dto.stockFront.WhAllocationDTO;
import com.rome.arch.core.clientobject.Response;

/**
 * 类WhAllocationRemoteService的实现描述：仓库调拨
 *
 * @author sunyj 2019/7/1 20:08
 */
@FeignClient(value = "scm-order-service")
public interface WhAllocationRemoteService {

    /**
     * 获取待下发订单
     * @param page
     * @param maxResult
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/getWaitSyncOrder", method = RequestMethod.POST)
    Response<List<WhAllocationDTO>> getWaitSyncOrder(@RequestParam("page") int page, @RequestParam("maxResult") int maxResult);

    /**
     * 下发PO单
     * @param order
     * @return
     */
    @RequestMapping(value = "/order/v1/wh_allocation/processWhAllocationOrderToSap", method = RequestMethod.POST)
    Response processWhAllocationOrderToSap(@RequestBody WhAllocationDTO order);
}
