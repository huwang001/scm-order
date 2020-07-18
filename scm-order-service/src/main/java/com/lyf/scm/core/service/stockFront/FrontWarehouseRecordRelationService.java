package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import com.lyf.scm.core.domain.entity.stockFront.*;
import com.lyf.scm.core.remote.stock.dto.ReservationDTO;

public interface FrontWarehouseRecordRelationService {

    /**
     * 保存前置单 后置单关系(试吃单)
     *
     * @param warehouseRecord
     * @param frontRecord
     */
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, AdjustForetasteE frontRecord);

    /**
     * 保存前置单 后置单关系(门店盘点单)
     *
     * @param warehouseRecord
     * @param frontRecordE
     */
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, ShopInventoryE frontRecordE);

    /**
     * 保存前置单 后置单关系(门店零售)
     *
     * @param warehouseRecord
     * @param frontRecordE
     */
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, ShopSaleE frontRecordE);

    /**
     * 保存前置单 后置单关系(门店零售退货)
     *
     * @param warehouseRecord
     * @param frontRecordE
     */
    public void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, SalesReturnE frontRecordE);

    /**
     * 保存前置单（门店调拨单） 后置单关系
     *
     * @param warehouseRecord
     * @param frontRecordE
     */
    void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecord, ShopAllocationE frontRecordE);

    /**
     * 保存前置单（退货单） 后置单关系
     *
     * @param warehouseRecordE
     * @param orderReturn
     */
    void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecordE, OrderReturnE orderReturn);

    /**
     * 保存前置单（门店退货单） 后置单关系
     *
     * @param *           @param warehouseRecordE
     * @param shopReturnE
     * @return void
     * @author Lucky
     * @date 2020/7/15  19:09
     */
    void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecordE, ShopReturnE shopReturnE);

    /**
     * 保存前置单（预约单） 后置单关系
     *
     * @param warehouseRecordE
     * @param orderE
     */
    void saveAddFrontRecordAndWarehouseRelation(WarehouseRecordE warehouseRecordE, OrderE orderE);
}
