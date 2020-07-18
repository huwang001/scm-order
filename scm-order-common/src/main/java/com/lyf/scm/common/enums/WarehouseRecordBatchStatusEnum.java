package com.lyf.scm.common.enums;

/**
 * 出入库单批次是否同步
 * 
 * @author lei.jin
 */
public enum WarehouseRecordBatchStatusEnum {

	/**
	 * 初始状态
	 */
	INIT(0, "初始"),

	/**
	 * 已完成
	 */
	COMPLETE(1, "已完成"),

	/**
	 * 异常
	 */
	EXCEPTION(2, "异常"),

	/**
	 * 不需处理
	 */
	NO_HANDLING(3, "不需处理");

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 描述
	 */
	private String desc;

	WarehouseRecordBatchStatusEnum(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}

}