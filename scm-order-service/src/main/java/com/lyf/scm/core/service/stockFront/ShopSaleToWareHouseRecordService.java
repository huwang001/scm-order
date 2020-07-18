package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopSaleE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

public interface ShopSaleToWareHouseRecordService {

    /**
     * 通过门店零售单 --> 创建出库单（包括明细；关系）
     *
     * @param frontRecordE
     * @return
     * @author zhanglong
     */
    WarehouseRecordE createWarehouseRecordByShopSale(ShopSaleE frontRecordE);
}
