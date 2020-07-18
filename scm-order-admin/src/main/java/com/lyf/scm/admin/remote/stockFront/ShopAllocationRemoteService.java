package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.ShopAllocationDetailDTO;
import com.lyf.scm.admin.remote.dto.ShopAllocationRecordPageDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description
 * @date 2020/6/18
 * @Version
 */
@FeignClient(value="scm-order-service")
public interface ShopAllocationRemoteService {

    /**
     * 查询门店调拨列表
     * @param frontRecord
     * @return
     */
    @RequestMapping(value = "/order/v1/shopAllocation/frontRecord/queryShopAllocationList", method = RequestMethod.POST)
    Response<PageInfo<ShopAllocationRecordPageDTO>> queryShopAllocationList(@RequestBody ShopAllocationRecordPageDTO frontRecord);

    /**
     * 查询门店调拨详情列表
     * @param frontRecordId
     * @return
     */
    @RequestMapping(value = "/order/v1/shopAllocation/frontRecord/queryShopAllocationDetailList", method = RequestMethod.GET)
    Response<List<ShopAllocationDetailDTO>> queryShopAllocationDetailList(@RequestParam("frontRecordId") Long frontRecordId);


}
