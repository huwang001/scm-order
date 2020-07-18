package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordCondition;
import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopSaleRecordDTO;

public interface ShopSaleService {

    /**
     * 创建门店销售单
     *
     * @param frontRecord
     * @return
     * @author zhanglong
     */
    void addShopSaleRecord(ShopSaleRecordDTO frontRecord);

    /**
     * 取消门店零售单
     *
     * @param outRecordCode
     * @author zhanglong
     */
    void cancelShopSaleRecord(String outRecordCode);

    /**
     * 根据页面条件查询出库单
     *
     * @param condition
     * @return
     * @author zhanglong
     */
    PageInfo<SaleWarehouseRecordDTO> queryWarehouseRecordList(SaleWarehouseRecordCondition condition);

    /**
     * 根据门店销售出库单查询详情
     *
     * @param warehouseRecordId
     * @return
     * @author zhanglong
     */
    SaleWarehouseRecordDTO querySaleWarehouseRecordInfoById(Long warehouseRecordId);
}
