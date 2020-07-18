package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存类型（虚仓流水查询）
 */
public enum StockTypeEnum {

	/**
	 * 增加真实库存(入库)
	 */
	TRANS_TYPE_WAREHOUSE_STOCK_INCREASE("增加真实库存", 1),
	/**
	 * 减少实际库存（出库）
	 */
	TRANS_TYPE_WAREHOUSE_STOCK_DECREASE("减少实际库存", 2),
	/**
	 * 实际库存冻结（出入）
	 */
	TRANS_TYPE_WAREHOUSE_STOCK_LOCK("实际库存冻结", 3),
	/**
	 * 实际库存解冻（出入）
	 */
	TRANS_TYPE_WAREHOUSE_STOCK_UNLOCK("实际库存解冻", 4),
	/**
	 * 真实库存出库（出库）
	 */
	TRANS_TYPE_WAREHOUSE_REAL_STOCK_OUT("真实库存出库", 5),
	/**
	 * 冻解库存出库（出库）
	 */
	TRANS_TYPE_WAREHOUSE_LOCK_STOCK_OUT("冻解库存出库", 6),
	/**
	 * 增加在途库存（入库）
	 */
	TRANS_TYPE_WAREHOUSE_ONROAD_STOCK_INCREASE("增加在途库存", 7),
	/**
	 * 减少在途库存（出库）
	 */
	TRANS_TYPE_WAREHOUSE_ONROAD_STOCK_DECREASE("减少在途库存", 8),
	/**
	 * 增加质检库存（入库）
	 */
	TRANS_TYPE_WAREHOUSE_QUALITY_STOCK_INCREASE("增加质检库存", 9),
	/**
	 * 减少质检库存（出库）
	 */
	TRANS_TYPE_WAREHOUSE_QUALITY_STOCK_DECREASE("减少质检库存", 10),
	/**
	 * 增加质检不合格库存（入库）
	 */
	TRANS_TYPE_WAREHOUSE_UNQUALIFIED_STOCK_INCREASE("增加质检不合格库存", 11),
	/**
	 * 减少质检不合格库存（出库）
	 */
	TRANS_TYPE_WAREHOUSE_UNQUALIFIED_STOCK_DECREASE("减少质检不合格库存", 12);

	/**
	 * 描述
	 */
	private String desc;
	/**
	 * 库存类型
	 */
	private Integer type;

	StockTypeEnum(String desc, Integer type) {
		this.desc = desc;
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getType() {
		return type;
	}

	/**
	 * 根据库存类型获取描述
	 *
	 * @param status
	 * @return
	 */
	public static String getDescByType(Integer status) {
		for (StockTypeEnum ele : values()) {
			if (ele.getType().equals(status)) {
				return ele.getDesc();
			}
		}
		return null;
	}

	/**
	 * 获取所有枚举的值-描述(出库)
	 *
	 * @return
	 */
	public static Map<Integer, String> getOutStockTypeList() {
		Map<Integer, String> map = new HashMap<>();
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_DECREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_DECREASE.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_LOCK.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_LOCK.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_UNLOCK.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_UNLOCK.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_REAL_STOCK_OUT.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_REAL_STOCK_OUT.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_LOCK_STOCK_OUT.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_LOCK_STOCK_OUT.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_ONROAD_STOCK_DECREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_ONROAD_STOCK_DECREASE.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_QUALITY_STOCK_DECREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_QUALITY_STOCK_DECREASE.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_UNQUALIFIED_STOCK_DECREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_UNQUALIFIED_STOCK_DECREASE.getDesc());
		return map;
	}

	/**
	 * 获取所有枚举的值-描述(入库)
	 *
	 * @return
	 */
	public static Map<Integer, String> getInStockTypeList() {
		Map<Integer, String> map = new HashMap<>();
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_INCREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_INCREASE.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_LOCK.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_LOCK.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_UNLOCK.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_STOCK_UNLOCK.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_ONROAD_STOCK_INCREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_ONROAD_STOCK_INCREASE.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_QUALITY_STOCK_INCREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_QUALITY_STOCK_INCREASE.getDesc());
		map.put(StockTypeEnum.TRANS_TYPE_WAREHOUSE_UNQUALIFIED_STOCK_INCREASE.getType(),
				StockTypeEnum.TRANS_TYPE_WAREHOUSE_UNQUALIFIED_STOCK_INCREASE.getDesc());
		return map;
	}

	/**
	 * 获取所有枚举的值
	 *
	 * @return
	 */
	public static Map<Integer, String> getAllStockTypeList() {
		Map<Integer, String> map = new HashMap<>(18);
		for (StockTypeEnum ele : values()) {
			map.put(ele.getType(), ele.getDesc());
		}
		return map;
	}
}
