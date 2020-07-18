package com.lyf.scm.common.constants;

import java.io.Serializable;

/**
 * @Description: wms对应仓库系统配置
 * <p>
 * @Author: chuwenchao  2019/6/2
 */
public class WmsConfigConstants implements Serializable {

    /**
     * 异常仓库系统编码
     */
    public static final int WMS_EX = -1;

    /**
     * 大福仓库系统编码
     */
    public static final int WMS_DF = 1;

    /**
     * 旺店通仓库系统编码
     */
    public static final int WMS_WDT = 2;

    /**
     * SAP仓库系统编码
     */
    public static final int WMS_SAP = 3;

    /**
     * 欧电云仓库系统编码
     */
    public static final int WMS_ODY = 4;

    /**
     * 邮政仓库系统编码
     */
    public static final int WMS_EMS = 5;

    /**
     * 商家仓库系统编码
     */
    public static final int WMS_MERCHANT = 6;

    /**
     * 玄天虚拟取消do单
     */
    public static final int WMS_VIRTUAL = 7;
    /**
     * 科捷wms
     */
    public static final int WMS_KJ = 8;

    /**
     * SAP发货单方法名
     */
    public static final String SAP_DELIVERY_ORDER = "DeliveryOrder";

    /**
     * SAP收货单方法名
     */
    public static final String SAP_RECEIPT_ORDER = "RecvOrder";

}
