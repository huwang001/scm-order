package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存类型枚举类
 */
public enum InventoryTypeEnum {

	/**
	 * 正品
	 */
	ZP(1, "ZP", "正品"),
	/**
	 * 残次
	 */
	CC(2, "CC", "残次"),
	/**
	 * 机损
	 */
	JS(3, "JS", "机损"),
	/**
	 * 箱损
	 */
	XS(4, "XS", "箱损"),
	/**
	 * 在途
	 */
	ZT(5, "ZT", "在途");

	private Integer type;

	private String typeStr;

	private String desc;

	InventoryTypeEnum(Integer type, String typeStr, String desc) {
		this.type = type;
		this.typeStr = typeStr;
		this.desc = desc;
	}

	public static Map<Integer, String> getInventoryType() {
		Map map = new HashMap<>();
		for (InventoryTypeEnum item : InventoryTypeEnum.values()) {
			map.put(item.getType(), item.getDesc());
		}
		return map;
	}

	public Integer getType() {
		return type;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public String getDesc() {
		return desc;
	}

	public static Integer getTypeByTypeStr(String typeStr) {
		for (InventoryTypeEnum InventoryTypeEnum : values()) {
			if (InventoryTypeEnum.getTypeStr().equals(typeStr)) {
				return InventoryTypeEnum.getType();
			}
		}
		return null;
	}

}
