package com.lyf.scm.core.remote.stock.dto;

import java.util.List;

import lombok.Data;

/**
 * 查询库存中心虚仓SKU权限
 * 
 * @author WWH
 *
 */
@Data
public class QueryVmSkuPermitDTO {

	/**
	 * 虚仓组ID集合
	 */
	private List<Long> groupIds;

	/**
	 * 商品ID集合
	 */
	private List<Long> skuIds;

	/**
	 * 虚仓是否有商品进货权 0没权限 1有权限
	 */
	private Integer isPermit;

}
