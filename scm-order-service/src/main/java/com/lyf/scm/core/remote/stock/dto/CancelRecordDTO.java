package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 取消出入库单（订单中心-库存中心）
 * 
 * @author WWH
 *
 */
@Data
public class CancelRecordDTO implements Serializable {

	private static final long serialVersionUID = -2511493284803084065L;
	/**
	 * 出入库单据编号
	 */
	private String recordCode;

	/**
	 * 出入库单据类型
	 */
	private Integer recordType;

	/**
	 * 是否强制取消 0否 1是
	 */
	private Integer isForceCancel;

}
