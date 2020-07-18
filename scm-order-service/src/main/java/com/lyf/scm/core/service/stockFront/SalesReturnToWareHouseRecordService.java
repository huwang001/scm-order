package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.SalesReturnE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

public interface SalesReturnToWareHouseRecordService {

    /**
     * 通过门店零售退货到哪 --> 创建出库单（包括明细；关系）
     *
     * @param frontRecordE
     * @return
     * @author zhanglong
     */
    WarehouseRecordE createWarehouseRecordBySalesReturn(SalesReturnE frontRecordE);


    /**
     * 保存后置单信息 明细 关系
     *
     * @param warehouseRecord
     * @param frontRecordE
     * @author zhanglong
     */
    void saveWarehouseRecordInfo(WarehouseRecordE warehouseRecord, SalesReturnE frontRecordE);
}
