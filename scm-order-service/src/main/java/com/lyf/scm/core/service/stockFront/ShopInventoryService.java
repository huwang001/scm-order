package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.InventoryRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryPageDTO;

import java.util.List;

/**
 * 门店盘点
 *
 * @author jl
 */
public interface ShopInventoryService {

    /**
     * 分页查询未处理门店盘点单
     *
     * @author zhanglong
     */
    List<Long> queryInitShopInventoryRecords(Integer startPage, Integer endPage);

    /**
     * 批量处理门店盘点单
     *
     * @author zhanglong
     */
    void handleShopInventoryRecords(Long id) throws Exception;

    /**
     * 批量创建门店盘点单
     *
     * @author zhanglong
     */
    void addShopInventoryRecords(List<InventoryRecordDTO> frontRecords);

    /**
     * 查询盘点单列表
     *
     * @author zhanglong
     */
    PageInfo<ShopInventoryPageDTO> queryShopInventoryList(ShopInventoryPageDTO frontRecord);


    /**
     * 查询盘点单详情列表
     *
     * @author zhanglong
     */
    List<ShopInventoryDetailDTO> queryShopInventoryDetailList(Long frontRecordId);
}
