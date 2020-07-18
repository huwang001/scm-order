package com.lyf.scm.common.enums;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zys
 * @Description 库存模型常量
 * @date 2020/6/12 10:25
 * @Version
 */
public class StockCoreConsts {

    /**
     * 0值，即比较中，常用这个值
     */
    public static final BigDecimal MIN_VALUE_ZERO = new BigDecimal("0.0009");


    /**
     * 小数点后位数，当前为3位
     */
    public static final int DECIMAL_POINT_NUM = 3;

    /**
     * 数据分割,下划线
     */
    public static final String DATA_SPLIT_UNDERLINE = "_";

    /**
     * 核对,门店批次库存,是否自动修复
     */
    public static final AtomicBoolean CHECK_SHOP_BATCH_STOCK_REPAIR_AUTO = new AtomicBoolean(false);

}
