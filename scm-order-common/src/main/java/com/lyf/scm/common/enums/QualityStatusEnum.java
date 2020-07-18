package com.lyf.scm.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 质检状态枚举
 */
public enum QualityStatusEnum {

	WITHOUT_QUALITY_INSPECTION(-1, "无需质检"), 
	FOR_QUALITY_INSPECTION(0, "待质检"), 
	QUALITY_INSPECTION_QUALIFIED(1,"质检合格"), 
	UNQUALIFIED_INSPECTION(2, "质检不合格");

	private Integer type;

	private String desc;

	QualityStatusEnum(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static Map<Integer, String> getQualityStatus() {
		Map map = new HashMap<>();
		for (QualityStatusEnum item : QualityStatusEnum.values()) {
			if (!item.getType().equals(-1)) {
				map.put(item.getType(), item.getDesc());
			}
		}
		return map;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
