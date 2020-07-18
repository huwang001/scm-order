package com.lyf.scm.common.constants;

import java.io.Serializable;

/**
 * @Description: 日志logType
 * <p>
 * @Author: chuwenchao  2019/8/20
 */
public class KibanaLogConstants implements Serializable {

    //======= Service Start =======
    /**
     * logType：外部接口调用超时日志
     */
    public static final String OUTER_SERVICE_TIME_OUT = "outerServiceTimeOut";
   
    /**
     * logType：内部接口调用超时日志
     */
    public static final String INNER_SERVICE_TIME_OUT = "innerServiceTimeOut";

    /**
     * logType：日志
     */
    public static final String CORE_LOG = "coreLog";

    /**
     * logType：门店补货单内部交易下发日志
     */
    public final static String SHOP_REPLENISH = "innerTradeCall";

    /**
     * logType：门店试吃调整单
     */
    public final static String SHOP_ADJUST_FORETASTE = "shopAdjustForetaste";

    /**
     * logType：门店盘点单
     */
    public final static String SHOP_INVENTORY = "shopInventory";

    /**
     * logType：门店盘点单处理
     */
    public final static String SHOP_INVENTORY_HANDLE = "shopInventoryHandle";

    /**
     * logType：门店零售单
     */
    public final static String SHOP_SALE = "shopSale";

    /**
     * logType：门店零售退货单
     */
    public final static String SALES_RETURN = "salesReturn";


    /**
     * logType:门店调拨
     */
    public final static String SHOP_ALLOCATION="shopAllocation";

    /**
     * logType：库存通知
     */
    public final static String STOCK_NOTIFY ="stockNotify";

    /**
     * logType：出库单同步到派车系统
     */
    public final static String DISPATCH_CAR = "dispatchCar";
    
    /**
     * logType：回滚异常
     */
    public final static String ROLL_BACK_EXCEPTION ="rollbackException";

    /**
     * logType：派车系统回调通知
     */
    public final static String TMS_NOTIFY ="tmsNotify";

    /**
     * APP下单
     */
    public final static String APP_CALL = "appCall";

    /**
     * logType：门店盘点单处理
     */
    public final static String PACK_TASK_OPERATION = "packTaskOperationFinish";
    
    /**
     * logType：包装需求单领料状态
     */
    public final static String PACK_DEMAND_PICK_STATUS = "packDemandPickStatus";
    
    /**
     * logType：取消预约单
     */
    public final static String CANCEL_ORDER = "cancelOrder";
    

    //======= Job Start =======


    //======= Admin Start =======

}
