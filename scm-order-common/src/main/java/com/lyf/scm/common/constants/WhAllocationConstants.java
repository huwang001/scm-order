package com.lyf.scm.common.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 类WhAllocationConstant的实现描述：仓库调拨常量
 *
 * @author sunyj 2019/5/30 17:31
 */
public class WhAllocationConstants {

	/**
	 * 内部调拨
	 */
	public static final Integer INNER_ALLOCATION = 1;

	/**
	 * 内部调拨
	 */
	public static final String INNER_ALLOCATION_STR = "内部调拨";

	/**
	 * RDC调拨
	 */
	public static final Integer RDC_ALLOCATION = 2;

	/**
	 * RDC调拨
	 */
	public static final String RDC_ALLOCATION_STR = "RDC调拨";

	/**
	 * 退货调拨
	 */
	public static final Integer RETURN_ALLOCATION = 3;

	/**
	 * 电商仓调拨
	 */
	public static final Integer E_COMMERCE_ALLOCATION = 4;

	/**
	 * 质量问题调拨
	 */
	public static final Integer UNQUALIFIED_ALLOCATION = 5;

	/**
	 * 调拨类型汇总
	 */
	public static final List<Integer> ALLOCATION_TYPE_LIST = Arrays.asList(1, 2);

	/**
	 * 调拨类型汇总
	 */
	public static final List<String> ALLOCATION_TYPE_LIST_STR = Arrays.asList(INNER_ALLOCATION_STR, RDC_ALLOCATION_STR);

	/**
	 * 已确认
	 */
	public static final Integer CONFIM_STATUS = 1;

	/**
	 * 无需下发
	 */
	public static final Integer NOT_NEED_SYNC_STATUS = 0;

	/**
	 * 待下发
	 */
	public static final Integer WAIT_SYNC_STATUS = 1;

	/**
	 * 出入都是中台
	 */
	public static final Integer WH_TYPE_INIT = 0;
	/**
	 * 出入都是中台
	 */
	public static final Integer WH_TYPE_ALL_MIDDLE = 1;

	/**
	 * 出中台入非中台
	 */
	public static final Integer WH_TYPE_OUT_MIDDLE = 2;

	/**
	 * 出非中台入中台
	 */
	public static final Integer WH_TYPE_IN_MIDDLE = 3;

	/**
	 * 退货调拨
	 */
	public static final Integer RETURN_ALLOCATE = 1;

	/**
	 * 退货调拨
	 */
	public static final String RETURN_ALLOCATE_STR = "是";

	/**
	 * 非退货调拨
	 */
	public static final Integer NOT_RETURN_ALLOCATE = 0;

	/**
	 * 非退货调拨
	 */
	public static final String NOT_RETURN_ALLOCATE_STR = "否";

	/**
	 * 退货调拨类型汇总
	 */
	public static final List<String> RETURN_ALLOCATE_TYPE_LIST_STR = Arrays.asList(RETURN_ALLOCATE_STR,
			NOT_RETURN_ALLOCATE_STR);

	/**
	 * 页面新增
	 */
	public static final Integer ADD_TYPE_PAGE = 1;

	/**
	 * excel导入新增
	 */
	public static final Integer ADD_TYPE_IMPORT = 2;

	/**
	 * 差异新增
	 */
	public static final Integer ADD_TYPE_DISPARITY = 3;
	
	/**
     * 基于销售预约单创建
     */
    public static final Integer ADD_TYPE_RESERVATION = 4;
    
    /**
     * 基于包装需求单创建
     */
    public static final Integer ADD_TYPE_PACK_DEMAND = 5;

	/**
	 * 存在差异
	 */
	public static final Integer IS_DISPARITY_TRUE = 1;

	/**
	 * 不存在差异
	 */
	public static final Integer IS_DISPARITY_FALSE = 0;

	/**
	 * 整箱
	 */
	public static final String SPLIT_TYPE_CONTAINER = "01";

	/**
	 * 拆零
	 */
	public static final String SPLIT_TYPE_SPLIT = "02";

	/**
	 * 支持整箱拆零拆单的工厂号
	 */
	public static final String SPLIT_ORDER_FACTORY_CODE = "X007";

	/**
	 * 质量调拨
	 */
	public static final Integer QUALITY_ALLOCATE = 1;

	/**
	 * 质量调拨
	 */
	public static final String QUALITY_ALLOCATE_STR = "是";

	/**
	 * 非质量调拨
	 */
	public static final Integer NOT_QUALITY_ALLOCATE = 0;

	/**
	 * 非质量调拨
	 */
	public static final String NOT_QUALITY_ALLOCATE_STR = "否";

	/**
	 * 基本单位类型
	 */
	public static final Integer BASIC_TYPE = 5;

	/**
	 * 发货单位类型
	 */
	public static final Integer TRANS_TYPE = 3;

}