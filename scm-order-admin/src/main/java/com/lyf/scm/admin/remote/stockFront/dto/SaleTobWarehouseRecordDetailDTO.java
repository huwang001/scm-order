package com.lyf.scm.admin.remote.stockFront.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 销售发货详情
 */
@Data
@EqualsAndHashCode
public class SaleTobWarehouseRecordDetailDTO {
	/**
	 * 唯一主键
	 */
	private Long id;
	/**
	 * 商品sku  Id
	 */
	private Long skuId;

	/**
	 * 商品sku    Code
	 */
	private String skuCode;
	/**
	 * 商品名称
	 */
	private String skuName;

	/**
	 * 商品数量
	 */
	private BigDecimal planQty;
	/**
	 * 实际收货数量 包括待质检的
	 */
	private BigDecimal actualQty;
	/**
	 * 基本计件单位名称
	 */
	private String unit;
	/**
	 * 基本计件单位
	 */
	private String unitCode;
	/**
	 * 实仓ID
	 */
	private Long realWarehouseId;

	/**
	 * 实仓ID
	 */
	private String realWarehouseName;
	/**
	 * 所属单据id
	 */
	private Long warehouseRecordId;
	
	/**
     * sap采购单号
     */
    private String sapPoNo;

    /**
     * 采购单行号
     */
    private String lineNo;

    /**
     * 交货单行号
     * */
    private String deliveryLineNo;
    
}
