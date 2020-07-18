package com.lyf.scm.job.remote.stockFront;

import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 门店盘点 查询
 */
@FeignClient(value = "scm-order-service")
public interface ShopInventoryRecordRemoteService {

    /**
     * 批量处理门店盘点单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/order/v1/shopInventory/frontRecord/handleShopInventoryRecords", method = RequestMethod.POST)
    Response<String> handleShopInventoryRecords(@RequestParam("id") Long id);


    /**
     * 分页查询未处理门店盘点单
     *
     * @param startPage
     * @param endPage
     * @return
     */
    @RequestMapping(value = "/order/v1/shopInventory/frontRecord/queryInitShopInventoryRecords", method = RequestMethod.GET)
    Response<List<Long>> queryInitShopInventoryRecords(@RequestParam("startPage") Integer startPage, @RequestParam("endPage") Integer endPage);

}
