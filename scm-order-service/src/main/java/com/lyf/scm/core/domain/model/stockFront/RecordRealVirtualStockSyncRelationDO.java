package com.lyf.scm.core.domain.model.stockFront;

import java.math.BigDecimal;

import com.lyf.scm.core.domain.model.common.BaseDO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordRealVirtualStockSyncRelationDO extends BaseDO {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 单据编号
	 */
	private String recordCode;

	/**
	 * 实体仓库id
	 */
	private Long realWarehouseId;

	/**
	 * 实仓编码
	 */
	private String realWarehouseCode;

	/**
	 * 工厂编码
	 */
	private String factoryCode;

	/**
	 * 虚拟仓库ID
	 */
	private Long virtualWarehouseId;

	/**
	 * 虚仓code
	 */
	private String virtualWarehouseCode;

	/**
	 * 商品sku编码
	 */
	private Long skuId;

	/**
	 * 同步比率（百分比），1-100区间可选数字
	 */
	private BigDecimal syncRate;

	/**
	 * 分配类型1比例分配 2绝对值分配
	 */
	private Integer allotType;

}