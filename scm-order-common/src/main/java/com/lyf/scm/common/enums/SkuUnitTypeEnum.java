package com.lyf.scm.common.enums;

/**
 * 单位枚举
 * 
 * @author zhangxu
 */
public enum SkuUnitTypeEnum {

	/**
	 * 库存单位
	 */
	STOCK_UNIT(1L, "库存单位"),

	/**
	 * 销售单位
	 */
	SALES_UNIT(2L, "销售单位"),

	/**
	 * 运输单位
	 */
	TRANSPORT_UNIT(3L, "运输单位"),

	/**
	 * 采购单位
	 */
	PURCHASE_UNIT(4L, "采购单位"),

	/**
	 * 基础单位
	 */
	BASIS_UNIT(5L, "基础单位"),

	/**
	 * 其他单位
	 */
	OTHER_UNIT(6L, "其他单位");

	private final Long id;

	private final String name;

	SkuUnitTypeEnum(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}