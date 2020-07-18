/**
 * Filename RealWarehouseStockDo.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * SKU库存信息
 * 
 * @author WWH
 *
 */
@Data
public class SkuStockDTO {

	/**
	 * 商品ID
	 */
	private Long skuId;

	/**
	 * 商品编码
	 */
	private String skuCode;

	/**
	 * 单位编码
	 */
	private String unitCode;

	/**
	 * 真实数量
	 */
	private BigDecimal availableQty;

	/**
	 * 渠道编码
	 */
	private String channelCode;

}