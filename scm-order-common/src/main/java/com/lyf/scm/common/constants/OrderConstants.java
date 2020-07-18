package com.lyf.scm.common.constants;

/**
 * @Description: 预约单常量
 * <p>
 * @Author: chuwenchao  2020/4/13
 */
public interface OrderConstants {
    /**
     * 需要包装
     */
    int NEED_PACKAGE = 1;
    /**
     * 不需要包装
     */
    int NO_NEED_PACKAGE = 0;
    
    /**
     * 初始状态
     */
    int INIT_STATUS = 0;
    
    /**
     * 锁定状态 1:部分锁定
     */
    int LOCK_STATUS_PART = 1;

    /**
     * 锁定状态 2:全部锁定
     */
    int LOCK_STATUS_ALL = 2;
    /**
     * 调拨状态 调拨出库
     */
    int ALLOT_STATUS_OUT = 11;
    /**
     * 调拨状态 调拨入库
     */
    int ALLOT_STATUS_IN = 12;
    /**
     * 加工状态 加工完成
     */
    int PROCESS_STATUS_DONE = 20;
    /**
     * 发货状态 待发货
     */
    int DELIVERY_STATUS_WAIT = 30;
    /**
     * 发货状态 已发货
     */
    int DELIVERY_STATUS_DONE = 31;
    /**
     * 取消状态
     */
    int CANCEL_STATUS = 40;

    /**
     * 同步交易状态 不需要
     */
    int SYNC_TRADE_STATUS_NO = 0;
    /**
     * 同步交易状态 待同步（锁定）
     */
    int SYNC_TRADE_STATUS_LOCK_WAIT = 1;
    /**
     * 同步交易状态 已同步（锁定）
     */
    int SYNC_TRADE_STATUS_LOCK_DONE = 2;
    /**
     * 同步交易状态 待同步（DO）
     */
    int SYNC_TRADE_STATUS_DO_WAIT = 10;
    /**
     * 同步交易状态 已同步（DO）
     */
    int SYNC_TRADE_STATUS_DO_DONE = 11;

    /**
     * 交易审核状态 通过
     */
    int TRADE_AUDIT_STATUS_PASSED = 1;
    /**
     * 交易审核状态 未通过
     */
    int TRADE_AUDIT_STATUS_NOT_PASS = 0;

    /**
     * 已创建DO
     */
    int HAS_DO = 1;

}
