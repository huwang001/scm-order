package com.lyf.scm.common.constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description: 公共常量
 * <p>
 * @Author: chuwenchao  2019/5/30
 */
public class CommonConstants implements Serializable {

    public static final String SUCCESS = "success";

    public static final String FAILURE = "failure";

    public static final String CODE_SUCCESS = "0";

    public static final String CODE_FAILURE = "1";

    public static final String SAP_SUCCESS = "S";

    public static final String SAP_FAILURE = "E";

	public static final String MQ_SEND_OK = "SEND_OK";

	public static final int BATCH_PARAM_MAX_LENGTH = 100;
    
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
