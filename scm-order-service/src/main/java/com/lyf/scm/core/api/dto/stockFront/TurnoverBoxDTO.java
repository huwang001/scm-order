package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: TurnoverBoxDTO
 *               <p>
 * @Author: chuwenchao 2019/7/31
 */
@Data
public class TurnoverBoxDTO implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 出入库单据编号
	 */
	private String recordCode;

	/**
	 * 物料号
	 */
	private String skuCode;

	/**
	 * 包材型号
	 */
	private String boxType;

	/**
	 * 包材数量
	 */
	private Integer boxQty;

	/**
	 * 单位编码
	 */
	private String unitCode;

}