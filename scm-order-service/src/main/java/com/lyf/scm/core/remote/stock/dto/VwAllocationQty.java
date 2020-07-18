package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @Doc:虚仓分配数
 * @Author: lchy
 * @Date: 2020/3/25
 * @Version 1.0
 */
@Data
public class VwAllocationQty {

	private Long virtualWarehouseId;

	/**
	 * 预计算的虚仓分配数[基本单位],入库用，入参操作时不能用realSkuQty,是空的
	 */
	private BigDecimal skuQty;

	/**
	 * 实际分配数[只有出库或锁定库存时会用到，入库操作不会用到,因为入库不涉及到库存不足的问题][基本单位]，做中间转换用的字段
	 */
	private BigDecimal realSkuQty;

}