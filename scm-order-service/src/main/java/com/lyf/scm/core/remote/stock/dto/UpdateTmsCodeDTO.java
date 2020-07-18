package com.lyf.scm.core.remote.stock.dto;

import java.util.List;

import lombok.Data;

/**
 * 修改派车单（订单中心-库存中心）
 * 
 * @author WWH
 *
 */
@Data
public class UpdateTmsCodeDTO {

	/**
	 * 操作人ID
	 */
	private Long userId;

	/**
	 * TMS派车单号
	 */
	private String tmsCode;

	/**
	 * 出库单单据编号集合
	 */
	private List<String> recordCodeList;

}
