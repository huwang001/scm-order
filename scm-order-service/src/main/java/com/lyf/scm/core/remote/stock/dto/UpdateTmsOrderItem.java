package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * @Description: 更新派车单号Item
 *               <p>
 * @Author: chuwenchao 2019/10/15
 */
@Data
public class UpdateTmsOrderItem {

	/**
	 * 中台出库单号
	 */
	private String deliveryOrderCode;

	/**
	 * 更新状态 fail失败 success成功
	 */
	private String upStatus;

}