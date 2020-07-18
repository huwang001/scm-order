package com.lyf.scm.core.domain.model.order;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

/**
 * @Description: 预约单虚仓转移记录表对象 <br>
 *
 * @Author chuwenchao 2020/4/8
 */
@Data
public class OrderVwMoveDO extends BaseDO {
	
	//columns START
	/**
	 * 唯一主键
	 */
	private Long id;
	/**
	 * 虚仓调拨单号
	 */
	private String vwMoveCode;
	/**
	 * 预约单号
	 */
	private String orderCode;
	/**
	 * 工厂编号
	 */
	private String factoryCode;
	/**
	 * 实仓code
	 */
	private String realWarehouseCode;
	/**
	 * 调入虚仓code
	 */
	private String inVwWarehouseCode;
	/**
	 * 调出虚仓code
	 */
	private String outVwWarehouseCode;

}

	
