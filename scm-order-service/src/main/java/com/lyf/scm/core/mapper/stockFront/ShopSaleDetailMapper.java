package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopSaleDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopSaleDetailMapper {

    /**
     * 保存销售前置单详情
     * 保存字段：
     */
    void saveFrSaleRecordDetails(@Param("saleDetailList") List<ShopSaleDetailE> saleDetailEList);
}