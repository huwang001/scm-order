/**
 * Filename SkuRealVirtualStockSyncRelationDO.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.core.remote.stock.dto;

import java.util.List;

import lombok.Data;

/**
 * sku虚仓实仓同步比率表
 * 
 * @author xly
 * @since 2019年4月22日 上午10:25:07
 */
@Data
public class SkuRealVirtualStockSyncRelation {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 商品skuId
	 */
	private Long skuId;

	/**
	 * sku编号
	 */
	private String skuCode;

	/**
	 * 实仓仓库ID
	 */
	private Long realWarehouseId;

	/**
	 * 虚拟仓库ID
	 */
	private Long virtualWarehouseId;

	/**
	 * 同步比率（百分比） 1-100区间可选数字
	 */
	private Integer syncRate;

	/**
	 * 商家id
	 */
	private Long merchantId;

	/**
	 * 实仓id集合
	 */
	private List<Long> realWarehouseIds;

}