package com.lyf.scm.core.remote.stock.dto;

import com.lyf.scm.common.constants.WhAllocationConstants;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Doc:计算虚仓分配数的入参
 * @Author: lchy
 * @Date: 2020/3/25
 * @Version 1.0
 */
@Data
public class AllocationCalQtyParam {

	private String lineNo;

	private Long skuId;

	private String skuCode;

	private BigDecimal planQty;

	private String unitCode;

	/**
	 * 销售单位比例
	 */
	private BigDecimal scale;

	/**
	 * 寻源后销售单位数量(寻源专用)
	 */
	private BigDecimal saleQty;

	/**
	 * 寻源后销售单位转换为基础单位数量(寻源专用)
	 */
	private BigDecimal saleBasicQty;

	/**
	 * 基础单位skuCode和type组合key
	 */
	public String getBaseSkuTypeKey() {
		return this.skuCode + "_" + WhAllocationConstants.BASIC_TYPE;
	}

	/**
	 * 发货单位skuCode和type组合key
	 */
	public String getTransSkuTypeKey() {
		return this.skuCode + "_" + WhAllocationConstants.TRANS_TYPE;
	}

	/**
	 * 基础单位组合key
	 */
	public String getSkuUnitCodeKey() {
		return this.skuCode + "_" + this.unitCode;
	}

}