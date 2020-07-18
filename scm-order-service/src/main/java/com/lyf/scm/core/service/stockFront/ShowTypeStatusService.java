package com.lyf.scm.core.service.stockFront;

import java.util.Map;

public interface ShowTypeStatusService {

	/**
	 * 获取前置单状态列表
	 *
	 * @return
	 */
	Map<Integer, String> getFrontRecordStatusList();

	/**
	 * 获取前置单类型列表
	 *
	 * @return
	 */
	Map<Integer, String> getFrontRecordTypeList();

	/**
	 * 获取出入单状态列表
	 *
	 * @return
	 */
	Map<Integer, String> getWarehouseRecordStatusList();

	/**
	 * 入库单页面状态列表
	 *
	 * @return
	 */
	Map<Integer, String> getInWarehouseRecordStatusList();

	/**
	 * 出库单页面状态列表
	 *
	 * @return
	 */
	Map<Integer, String> getOutWarehouseRecordStatusList();

	/**
	 * 查找数据库中已存在的所有非Z补货出库单的状态
	 *
	 * @return
	 */
	Map<Integer, String> getReplenishOutWarehouseRecordStatusList();

	/**
	 * 获取出入单类型列表
	 *
	 * @return
	 */
	Map<Integer, String> getWarehouseRecordTypeList();

	/**
	 * 入库单页面类型列表
	 *
	 * @return
	 */
	Map<Integer, String> getInWarehouseRecordTypeList();

	/**
	 * 出库单页面类型列表
	 *
	 * @return
	 */
	Map<Integer, String> getOutWarehouseRecordTypeList();

	/**
	 * 获取加工单类型列表
	 *
	 * @return
	 */
	Map<Integer, String> getWarehouseAssembleRecordTypeList();

	/**
	 * 非Z补货出库单列表
	 *
	 * @return
	 */
	Map<Integer, String> getReplenishOutWarehouseRecordList();

	/**
	 * 获取所有实仓流水库存类型(出库)
	 * 
	 * @return
	 */
	Map<Integer, String> getOutStockType();

	/**
	 * 获取所有实仓流水库存类型(入库)
	 * 
	 * @return
	 */
	Map<Integer, String> getInStockType();

	/**
	 * 获取所有实仓流水库存类型
	 * 
	 * @return
	 */
	Map<Integer, String> getAllStockType();

	Map<Integer, String> getInventoryType();

	Map<Integer, String> getQualityStatus();

	/**
	 * 获取wms同步状态列表
	 * 
	 * @return
	 */
	Map<Integer, String> getWmsSyncStatusList();

}