package com.lyf.scm.core.mapper.shopReturn;

import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopReturnDetailMapper {

    List<ShopReturnDetailE> selectByRecordCode(String recordCode);

    /**
     * 插入门店退货单明细
     *
     * @param * @param record
     * @return int
     * @author Lucky
     * @date 2020/7/15  17:37
     */
    int insert(ShopReturnDetailE record);

    /**
     * 批量插入门店退货单明细
     *
     * @param * @param orderDetailEList
     * @return int
     * @author Lucky
     * @date 2020/7/15  17:36
     */
    int batchInsertDetail(@Param("batchList") List<ShopReturnDetailE> shopReturnDetailEList);

    /**
     * 批量更新 实际出库数量
     *
     * @author zhanglong
     * @date 2020/7/15 11:58
     */
    void updateRealRefundQty(@Param("frontRecordDetails") List<ShopReturnDetailE> updateDetail);

    /**
     * 批量更新 实际入库数量
     *
     * @author zhanglong
     * @date 2020/7/15 11:59
     */
    void updateRealEnterQty(@Param("frontRecordDetails") List<ShopReturnDetailE> updateDetail);
}