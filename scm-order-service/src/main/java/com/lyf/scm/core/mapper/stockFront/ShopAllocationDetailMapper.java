package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @date 2020/6/16
 * @Version
 */
public interface ShopAllocationDetailMapper {

    /**
     * 保存调拨单详情
     * 插入字段：
     * @param details
     */
    void saveShopAllocationRecordDetails(@Param("frShopAllocationDetails") List<ShopAllocationDetailE> details);

    /**
     * 根据前置单id查询调拨单详情
     * @param frontRecordId
     * @return
     */
    List<ShopAllocationDetailE> queryShopAllocationDetailList(@Param("frontRecordId") Long frontRecordId);
}
