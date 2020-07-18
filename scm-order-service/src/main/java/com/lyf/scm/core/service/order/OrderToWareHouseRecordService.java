package com.lyf.scm.core.service.order;

import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

/**
 * @Description 创建出库单service
 * @Author wuyuanhang
 * @Date 2020/7/1
 */
public interface OrderToWareHouseRecordService {


    /**
     * 根据前置单 创建出库单
     * @param orderE
     * @return
     */
    WarehouseRecordE createOutRecordByFrontRecord(OrderE orderE);
}
