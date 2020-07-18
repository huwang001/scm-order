package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.shop.ShopAdjustRecordDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 调整单
 */
@FeignClient(value = "scm-order-service")
public interface ShopAdjustRemoteService {

    @PostMapping("/order/v1/adjust/pageShopAdjustForetaste")
    Response<PageInfo<ShopAdjustRecordDTO>> findShopForetasteCondition(@RequestBody ShopAdjustRecordDTO adjustRecordDTO,
                                                                       @RequestParam("pageNum") Integer pageNum,
                                                                       @RequestParam("pageSize") Integer pageSize);

    @RequestMapping(value = "/order/v1/adjust/getAdjustForetasteByRecordId", method = RequestMethod.GET)
    Response<ShopAdjustRecordDTO> getAdjustForetasteByRecordId(@RequestParam("recordId") Long recordId);
}
