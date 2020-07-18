package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopInventoryDetailMapper {

    /**
     * 批量保存盘点单详情
     * 保存字段：record_code,front_record_id,sku_id,sku_qty,unit,acc_qty,diff_qty
     *
     * @param frShopInventoryDetails
     */
    void insertShopInventoryDetails(@Param("frShopInventoryDetails") List<ShopInventoryDetailE> frShopInventoryDetails);

    /**
     * 根据前置单id获取盘点详情
     *
     * @param frontRecordId
     * @return
     */
    List<ShopInventoryDetailE> queryShopInventoryDetailList(Long frontRecordId);

    /**
     * 根据前置单id查询详情
     *
     * @param id
     * @return
     */
    List<ShopInventoryDetailE> queryShopInventoryDetailListById(Long id);

    /**
     * 根据前置单id查询明细
     *
     * @param frontRecordIds
     * @return
     */
    List<ShopInventoryDetailE> queryShopInventoryDetailListByIds(@Param("frontRecordIds") List<Long> frontRecordIds);
}
