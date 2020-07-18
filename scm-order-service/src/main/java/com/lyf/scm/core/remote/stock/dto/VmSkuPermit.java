package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * @Description: ChangeLogDO
 *               <p>
 * @Author: chuwenchao 2019/10/12
 */
@Data
public class VmSkuPermit {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 虚仓ID
	 */
	private Long virtualWarehouseGroupId;

	/**
	 * 商品ID
	 */
	private Long skuId;

	/**
	 * 商品编码
	 */
	private String skuCode;

	/**
	 * 虚仓是否有商品进货权 0没权限 1有权限
	 */
	private Integer isPermit;

}