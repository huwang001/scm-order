package com.lyf.scm.core.domain.model.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecordStatusLogDO extends BaseDO implements Serializable{
	
	//columns START
	/**
	 * 唯一主键
	 */
	private Long id;
	/**
	 * 预约单号
	 */
	private String orderCode;
	/**
	 * 单据流转状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消
	 */
	private Integer recordStatus;

	public RecordStatusLogDO(String orderCode, Integer recordStatus) {
		this.orderCode = orderCode;
		this.recordStatus = recordStatus;
	}

	public RecordStatusLogDO() {
	}
}

	
