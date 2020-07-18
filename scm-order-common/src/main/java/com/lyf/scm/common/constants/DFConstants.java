package com.lyf.scm.common.constants;

import java.io.Serializable;

/**
 * 大福常量定义
 *
 * @author zhanglong
 * @date 2020/7/16 14:33
 */
public class DFConstants implements Serializable {
    /**
     * 门店发货
     */
    public static final String DF_SHOP = "10";

    /**
     * 委外出库
     */
    public static final String DF_OUTSOURCE = "20";

    /**
     * 加盟商发货
     */
    public static final String DF_REPLENISH = "50";

    /**
     * 退厂
     */
    public static final String DF_PURCHASE_OUT = "70";

    /**
     * 调拨出库
     */
    public static final String DF_ALLOCATION = "60";

    /**
     * 采购单类型：1、SP/食品类采购订单 2.FS/非食品类采购订单
     */
    public static final Integer DF_PURCHASE_TYPE_SP = 1;

    /**
     * 采购单类型：1、SP/食品类采购订单 2.FS/非食品类采购订单
     */
    public static final Integer DF_PURCHASE_TYPE_FS = 2;
}
