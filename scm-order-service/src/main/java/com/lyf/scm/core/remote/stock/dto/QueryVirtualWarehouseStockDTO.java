package com.lyf.scm.core.remote.stock.dto;

import java.util.List;

import lombok.Data;

/**
 * 查询库存中心虚仓库存
 * 
 * @author WWH
 *
 */
@Data
public class QueryVirtualWarehouseStockDTO {

	/**
	 * 虚仓编码
	 */
	private String virtualWarehouseCode;

	/**
	 * 商品ID集合
	 */
	private List<Long> skuIds;

}
