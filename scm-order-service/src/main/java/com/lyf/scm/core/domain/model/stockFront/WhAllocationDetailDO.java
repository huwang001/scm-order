package com.lyf.scm.core.domain.model.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;

import lombok.Data;

/**
 * 
 * @author <sunyj> 门店调拨明细
 * @version 2019-05-12 17:32:49
 * 
 */

@Data
public class WhAllocationDetailDO extends SkuQtyUnitBase implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 所属单据编码
	 */
	private String recordCode;

	/**
	 * 单据id
	 */
	private Long frontRecordId;

	/**
	 * 商品sku编码
	 */
	private Long skuId;

	/**
	 * 商品sku编码
	 */
	//private String skuCode;

	/**
	 * 调拨数量
	 */
	private BigDecimal allotQty;

	/**
	 * 实际调入数量
	 */
	private BigDecimal inQty;

	/**
	 * 实际调出数量
	 */
	private BigDecimal outQty;

	/**
	 * 批次备注
	 */
	private String batchRemark;

	/**
	 * 调拨单位
	 */
	private String unit;

	/**
	 * 单位code
	 */
	//private String unitCode;

	/**
	 * 退货原因
	 */
	private String reasonCode;

	/**
	 * 行号
	 */
	private String lineNo;

	private String originLineNo;

	/**
	 * 初始数量
	 */
	private BigDecimal orginQty;
	
}