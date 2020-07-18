package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

import java.util.List;

public interface ShopInventoryToWareHouseRecordService {

    /**
     * 通过门店盘点单 --> 创建出库单（包括明细；关系）
     *
     * @param frontRecordE
     * @return
     * @author zhanglong
     */
    WarehouseRecordE createOutWarehouseRecordByShopInventory(ShopInventoryE frontRecordE);

    /**
     * 通过门店盘点单 --> 创建入库单（包括明细；关系）
     *
     * @param frontRecordE
     * @return
     * @author zhanglong
     */
    WarehouseRecordE createInWarehouseRecordByShopInventory(ShopInventoryE frontRecordE);

    /**
     * 根据前置单id（门店盘点单）查询出入库单(包括详情)
     *
     * @param frontRecordId
     * @return
     * @author zhanglong
     */
    List<WarehouseRecordE> queryInventoryWarehouseById(Long frontRecordId);
}
