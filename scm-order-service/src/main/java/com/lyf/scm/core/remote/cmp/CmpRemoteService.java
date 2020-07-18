package com.lyf.scm.core.remote.cmp;

import com.lyf.scm.core.remote.cmp.dto.ShopAllocationRecordDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Description cmp下发
 * @date 2020/6/17 14:58
 * @Version
 */
@FeignClient(value = "arch-core-cmp-proxy", url = "${feignClient.trade.cmp-proxy.url:}")
public interface CmpRemoteService {

    /**
     * 门店调拨下发cmp
     * @param records
     * @return
     */
    @RequestMapping(value = "/cmp/v1/shop_inventory/pushAllocation",method = RequestMethod.POST)
    Response shopAllocationPushCMP(@RequestBody List<ShopAllocationRecordDTO> records);
}
