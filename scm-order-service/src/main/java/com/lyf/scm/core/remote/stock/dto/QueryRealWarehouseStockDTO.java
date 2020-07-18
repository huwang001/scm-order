package com.lyf.scm.core.remote.stock.dto;

import java.util.List;

import lombok.Data;

/**
 * 查询库存中心实仓库存
 * 
 * @author WWH
 *
 */
@Data
public class QueryRealWarehouseStockDTO {

	/**
	 * 工厂编码
	 */
	private String factoryCode;

	/**
	 * 仓库外部编码
	 */
	private String realWarehouseOutCode;

	/**
	 * 是否质量问题调拨 0不是 1是
	 */
	private Integer isQualityAllocate;

	/**
	 * 商品ID集合
	 */
	private List<Long> skuIds;

}
