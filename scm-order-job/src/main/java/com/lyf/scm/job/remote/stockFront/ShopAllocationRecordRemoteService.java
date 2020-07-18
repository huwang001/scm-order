package com.lyf.scm.job.remote.stockFront;

/**
 * @Description  门店调拨
 * @date 2020/6/19
 */

import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "scm-order-service")
public interface ShopAllocationRecordRemoteService {

    @RequestMapping(value = "/order/v1/shopAllocation/warehouseRecord/handleShopAllocationRecordsPushCmp", method = RequestMethod.POST)
    Response<String> handleShopAllocationRecordsPushCmp();
}
