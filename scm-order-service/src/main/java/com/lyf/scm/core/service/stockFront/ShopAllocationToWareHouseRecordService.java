package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

/**
 * @Description
 * @date 2020/6/16
 * @Version
 */
public interface ShopAllocationToWareHouseRecordService {

    /**
     * 根据门店调拨单 创建出库单
     */
    WarehouseRecordE createOutRecordByFrontRecord(ShopAllocationE frontRecordE);

    /**
     * 根据门店调拨单 创建入库单
     * @param frontRecordE
     */
    WarehouseRecordE createInRecordByFrontRecord(ShopAllocationE frontRecordE);
}
