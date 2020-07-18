package com.lyf.scm.core.service.common;


import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

import java.util.List;


/**
 * 实仓调拨
 */
public interface RealWarehouseAllocationService {

    /**
     * 根据工厂编号和仓库类型查询对应仓库
     * @param factoryCode
     * @param type
     * @return
     */
    List<RealWarehouse> queryRealWarehouseByFactoryCodeAndType(String factoryCode, Integer type);

    /**
     * 实仓调拨
     * @param orderCode
     * @param realWarehouseCode
     * @param userId
     * @return
     */
    void realWarehouseAllocation(String orderCode, String realWarehouseCode, Long userId);

    /**
     * 实仓调拨单出库通知
     * @param allotCode
     */
    void realWarehouseAllocationOutNotify(String allotCode);

    /**
     * 实仓调拨单入库通知
     * @param allotCode
     */
    void realWarehouseAllocationInNotify(String allotCode);

    /**
     * 团购仓出库发货回调
     * @param outRecordCode
     */
    void deliverOutNotifyToGroupPurchase(String outRecordCode);
}