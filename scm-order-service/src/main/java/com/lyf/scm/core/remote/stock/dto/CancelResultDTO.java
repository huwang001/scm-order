package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * 取消出入库单返回结果（库存中心-订单中心）
 * 
 * @author WWH
 *
 */
@Data
public class CancelResultDTO {

	/**
	 * 出入库单据编号
	 */
	private String recordCode;

	/**
	 * 出入库单据类型
	 */
	private Boolean status;

}
