package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @Description: RecordPackageDetail
 *               <p>
 * @Author: chuwenchao 2019/9/5
 */
@Data
public class RecordPackageDetailDTO implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 包裹ID
	 */
	private Long packageId;

	/**
	 * 包裹编号
	 */
	private String packageCode;

	/**
	 * 出入库单据编号
	 */
	private String recordCode;

	/**
	 * 商品sku编码
	 */
	private Long skuId;

	/**
	 * 商品编码
	 */
	private String skuCode;

	/**
	 * 商品条码
	 */
	private String barCode;

	/**
	 * 商品数量
	 */
	private BigDecimal skuQty;

}