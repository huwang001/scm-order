package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.ShopInventoryDetailDTO;
import com.lyf.scm.admin.remote.dto.ShopInventoryPageDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 门店盘点
 */
@FeignClient(value = "scm-order-service")
public interface ShopInventoryRemoteService {

    /**
     * 查询门店盘点列表
     *
     * @param frontRecord
     * @return
     */
    @RequestMapping(value = "/order/v1/shopInventory/frontRecord/queryShopInventoryList", method = RequestMethod.POST)
    Response<PageInfo<ShopInventoryPageDTO>> queryShopInventoryList(@RequestBody ShopInventoryPageDTO frontRecord);

    /**
     * 查询门店盘点详情列表
     *
     * @param frontRecordId
     * @return
     */
    @RequestMapping(value = "/order/v1/shopInventory/frontRecord/queryShopInventoryDetailList", method = RequestMethod.GET)
    Response<List<ShopInventoryDetailDTO>> queryShopInventoryDetailList(@RequestParam("frontRecordId") Long frontRecordId);
}
