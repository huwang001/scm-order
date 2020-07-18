package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.SalesReturnDetailE;

import java.util.List;

public interface SalesReturnDetailMapper {

    /**
     * 保存退货单详情
     * 保存字段：record_code,front_record_id,sku_id,sku_qty,unit
     */
    void saveFrSalesReturnRecordDetail(List<SalesReturnDetailE> frSalesReturnDOList);
}