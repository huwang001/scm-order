package com.lyf.scm.common.constants;

/**
 * 类WarehouseRecordConstant的实现描述：出入库单枚举
 *
 * @author sunyj 2019/6/16 17:07
 */
public class WarehouseRecordConstant {

	/**
	 * 0无需派车
	 */
	public static Integer INIT_DISPATCH = 0;

	/**
	 * 1待下发派车
	 */
	public static Integer NEED_DISPATCH = 1;

	/**
	 * 2已下发派车
	 */
	public static Integer DISPATCH_SUCCES = 2;

	/**
	 * 0无需过账
	 */
	public static Integer INIT_TRANSFER = 0;

	/**
	 * 1待下发过账
	 */
	public static Integer NEED_TRANSFER = 1;

	/**
	 * 2已下发过账
	 */
	public static Integer TRANSFER_SUCCES = 2;

	/**
	 * 0无需同步cmp
	 */
	public static Integer INIT_CMP = 0;

	/**
	 * 1待同步cmp
	 */
	public static Integer NEED_CMP = 1;

	/**
	 * 2已同步cmp
	 */
	public static Integer CMP_SUCCES = 2;
	/**
	 * 0无需推送交易
	 */
	public static Integer INIT_SYNC_TRADE = 0;

	/**
	 * 1待推送交易
	 */
	public static Integer NEED_SYNC_TRADE = 1;

	/**
	 * 2已推送交易
	 */
	public static Integer TRADE_SYNC_SUCCES = 2;

	/**
	 * 1待推送交易
	 */
	public static Integer SYNC_WMS_SUCCESS = 2;

	/**
	 * 0无需推送派车
	 */
	public static Integer INIT_SYNC_TMSB = 0;

	/**
	 * 1待推送派车
	 */
	public static Integer NEED_SYNC_TMSB = 1;

	/**
	 * 2已推送派车
	 */
	public static Integer SUCCESS_SYNC_TMSB = 2;
}