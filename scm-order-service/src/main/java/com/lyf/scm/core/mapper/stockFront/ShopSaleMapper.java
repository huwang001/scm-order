package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopSaleE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopSaleMapper {

    /**
     * 根据outRecordCode查找
     *
     * @param outRecordCode
     * @return
     * @throws Exception
     */
    ShopSaleE selectFrSaleRecordByOutRecordCode(String outRecordCode);

    /**
     * 根据outRecordCode批量查找
     *
     * @param outRecordCodeList
     * @return
     */
    List<ShopSaleE> selectFrSaleRecordByOutRecordCodeList(List<String> outRecordCodeList);

    /**
     * 保存
     */
    void saveFrSaleRecord(ShopSaleE entity);

    /**
     * 根据前置SO单id状态为已取消
     *
     * @param id 前置SO单主键
     * @return
     */
    int updateToCanceled(@Param("id") Long id, @Param("afterRecordStatus") Integer afterRecordStatus);

    /**
     * 通过Id批量查询门店零售单
     *
     * @param idList
     * @return
     */
    List<ShopSaleE> batchQueryShopSalesByIds(List<Long> idList);
}