package com.lyf.scm.common.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 类ShopReplenishConsts的实现描述：门店补货的枚举类
 *
 * @author sunyj 2019/5/31 16:43
 */
public class ShopReplenishConstant {

    /**
     * 直营采购
     */
    public static Integer DIRECT_REPLENISH = 1;

    /**
     * 加盟采购
     */
    public static Integer JOIN_REPLENISH = 2;

    /**
     * 供应商直送
     */
    public static Integer SUPPLIER_DIRECT_REPLENISH = 3;

    /**
     * 冷链
     */
    public static Integer COLD_CHAIN_REPLENISH = 4;

    /**
     * 加盟主配
     */
    public static Integer ALLIANCE_BUSINESS_REPLENISH = 5;

    /**
     * 加盟托管叫货
     */
    public static Integer JOIN_TRUSTEESHIP_REPLENISH = 6;

    /**
     * 允许的单据类型
     */
    public static List<Integer> ALLOW_RECORD_TYPE = Arrays.asList(1,2,3,4,5);

    /**
     * 普通配货
     */
    public static Integer COMMON_REPLENISH = 1;

    /**
     * 指定仓库配货
     */
    public static Integer ASSIGN_WH_REPLENISH = 2;

    /**
     * 直营
     */
    public static Integer DIRECT_SHOP_TYPE = 1;

    /**
     * 加盟
     */
    public static Integer JOIN_SHOP_TYPE = 3;

    /**
     * 加盟联营
     */
    public static Integer ALLIANCE_BUSINESS_SHOP_TYPE = 5;


    public static List<Integer> CAN_CREATE_COLD_CHAIN = Arrays.asList(1,2,4);

    /**
     * 0: 无需分配
     */
    public static Integer NOT_NEED_ALLOT = 0;

    /**
     * 1.需要分配
     */
    public static Integer NEED_ALLOT = 1;

    /**
     * 2. 分配完成
     */
    public static Integer ALLOT_SUCCESS = 2;

    /**
     * 0.待分配状态
     */
    public static Integer INIT_DISPATCH = 0;


    /**
     * 1.派车
     */
    public static Integer NEED_DISPATCH = 1;

    /**
     * 2.自提
     */
    public static Integer NOT_NEED_DISPATCH = 2;
}
