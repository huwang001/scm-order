package com.lyf.scm.core.domain.model.order;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderVwMoveDetailDO extends BaseDO {
	
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
	 * 商品编码
	 */
	private String skuCode;
	/**
	 * 转移数量
	 */
	private BigDecimal moveQty;
	/**
	 * 单位名称
	 */
	private String unit;
	/**
	 * 单位编码
	 */
	private String unitCode;

}

	
