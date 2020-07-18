package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * 下发SAP_PO_NO（订单中心-库存中心）
 * 
 * @author WWH
 *
 */
@Data
public class PoNoDTO {

	/**
	 * 单据编号
	 */
	private String recordCode;

	/**
	 * 交货单行号
	 */
	private String deliveryLineNo;

	/**
	 * 行号
	 */
	private String lineNo;

	/**
	 * SAP_PO_NO
	 */
	private String sapPoNo;

	/**
	 * 商品编码
	 */
	private String skuCode;

}
