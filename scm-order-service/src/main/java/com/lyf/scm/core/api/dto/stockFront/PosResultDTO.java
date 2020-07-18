/**
 * Filename RealWarehouseStockDo.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.api.dto.stockFront;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 移动POS查询移动POS盘点物料清单
 */
@Data
public class PosResultDTO {

	/**
	 * 门店编号
	 */
	private String shopCode;

	/**
	 * 商品sku编码
	 */
	private Long skuId;

	/**
	 * 可用库存
	 */
	private BigDecimal availableQty;

	/**
	 * sku编号
	 */
	private String skuCode;

	/**
	 * sku名称
	 */
	private String skuName;

	/**
	 * sku基本单位
	 */
	private String unitCode;

}