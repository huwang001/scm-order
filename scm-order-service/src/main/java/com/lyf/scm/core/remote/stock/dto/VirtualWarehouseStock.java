package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 类BaseDo的实现描述：虚拟库存Do表
 *
 * @author sunyj 2019/4/16 11:59
 */
@Data
public class VirtualWarehouseStock {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 虚拟仓库ID
	 */
	private Long virtualWarehouseId;

	/**
	 * 商品sku编码
	 */
	private Long skuId;

	/**
	 * 商品编码
	 */
	private String skuCode;

	/**
	 * 真实库存
	 */
	private BigDecimal realQty;

	/**
	 * 锁定库存
	 */
	private BigDecimal lockQty;

	/**
	 * 商家id
	 */
	private Long merchantId;

	/**
	 * 不可用库存
	 */
	private BigDecimal unUseQty;

	/**
	 * 可用库存
	 */
	private BigDecimal availableQty;

}