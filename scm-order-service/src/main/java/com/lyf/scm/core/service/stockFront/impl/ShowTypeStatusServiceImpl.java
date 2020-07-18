package com.lyf.scm.core.service.stockFront.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.lyf.scm.common.enums.FrontRecordStatusEnum;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.enums.InventoryTypeEnum;
import com.lyf.scm.common.enums.QualityStatusEnum;
import com.lyf.scm.common.enums.StockTypeEnum;
import com.lyf.scm.common.enums.WarehouseRecordStatusEnum;
import com.lyf.scm.common.enums.WarehouseRecordTypeEnum;
import com.lyf.scm.common.enums.WmsSyncStatusEnum;
import com.lyf.scm.core.service.stockFront.ShowTypeStatusService;

/**
 * 获取状态和类型集合
 */

@Service
public class ShowTypeStatusServiceImpl implements ShowTypeStatusService {

	@Override
	public Map<Integer, String> getFrontRecordStatusList() {
		Map<Integer, String> map = FrontRecordStatusEnum.getFrontRecordStatusList();
		return map;
	}

	@Override
	public Map<Integer, String> getFrontRecordTypeList() {
		Map<Integer, String> map = FrontRecordTypeEnum.getFrontRecordTypeList();
		return map;
	}

	@Override
	public Map<Integer, String> getWarehouseRecordStatusList() {
		Map<Integer, String> map = WarehouseRecordStatusEnum.getWarehouseRecordStatusList();
		return map;
	}

	@Override
	public Map<Integer, String> getInWarehouseRecordStatusList() {
		return this.getWarehouseRecordStatusList();
	}

	@Override
	public Map<Integer, String> getOutWarehouseRecordStatusList() {
		return this.getWarehouseRecordStatusList();
	}

	/**
	 * 查找数据库中已存在的所有非Z补货出库单的状态
	 *
	 * @return
	 */
	@Override
	public Map<Integer, String> getReplenishOutWarehouseRecordStatusList() {
		return this.getWarehouseRecordStatusList();
	}

	@Override
	public Map<Integer, String> getWarehouseRecordTypeList() {
		Map<Integer, String> map = WarehouseRecordTypeEnum.getWarehouseRecordTypeList();
		return map;
	}

	@Override
	public Map<Integer, String> getInWarehouseRecordTypeList() {
		Map<Integer, String> map = WarehouseRecordTypeEnum.getInWarehouseRecordPageTypeList();
		return map;
	}

	@Override
	public Map<Integer, String> getOutWarehouseRecordTypeList() {
		Map<Integer, String> map = WarehouseRecordTypeEnum.getOutWarehouseRecordPageTypeList();
		return map;
	}

	@Override
	public Map<Integer, String> getWarehouseAssembleRecordTypeList() {
		Map<Integer, String> map = FrontRecordTypeEnum.getWarehouseAssembleRecordTypeList();
		return map;
	}

	/**
	 * 非Z补货出库单列表
	 *
	 * @return
	 */
	@Override
	public Map<Integer, String> getReplenishOutWarehouseRecordList() {
		Map<Integer, String> map = WarehouseRecordTypeEnum.getReplenishOutWarehouseRecordList();
		return map;
	}

	/**
	 * 获取实仓流水库存类型(出库)
	 * 
	 * @return
	 */
	@Override
	public Map<Integer, String> getOutStockType() {
		Map<Integer, String> map = StockTypeEnum.getOutStockTypeList();
		return map;
	}

	/**
	 * 获取实仓流水库存类型(入库)
	 * 
	 * @return
	 */
	@Override
	public Map<Integer, String> getInStockType() {
		Map<Integer, String> map = StockTypeEnum.getInStockTypeList();
		return map;
	}

	/**
	 * 获取实仓流水库存类型
	 * 
	 * @return
	 */
	@Override
	public Map<Integer, String> getAllStockType() {
		Map<Integer, String> map = StockTypeEnum.getAllStockTypeList();
		return map;
	}

	/**
	 * 获取所有库存类型枚举
	 * 
	 * @return
	 */
	@Override
	public Map<Integer, String> getInventoryType() {
		Map<Integer, String> map = InventoryTypeEnum.getInventoryType();
		return map;
	}

	/**
	 * 获取所有质检状态枚举
	 * 
	 * @return
	 */
	@Override
	public Map<Integer, String> getQualityStatus() {
		Map<Integer, String> map = QualityStatusEnum.getQualityStatus();
		return map;
	}

	/**
	 * 获取wms同步状态列表
	 *
	 * @return
	 */
	@Override
	public Map<Integer, String> getWmsSyncStatusList() {
		Map<Integer, String> map = WmsSyncStatusEnum.getWmsSyncStatusList();
		return map;
	}
}
