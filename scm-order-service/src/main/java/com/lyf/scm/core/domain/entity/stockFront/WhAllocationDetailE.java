package com.lyf.scm.core.domain.entity.stockFront;

import java.math.BigDecimal;

import com.lyf.scm.core.domain.model.stockFront.WhAllocationDetailDO;

import lombok.Data;

/**
 * 类WhAllocationDetailE的实现描述：调拨明细
 *
 * @author sunyj 2019/5/13 11:59
 */
@Data
public class WhAllocationDetailE extends WhAllocationDetailDO {

	private BigDecimal planOrigin;

	private String planUnitCode;

	/**
	 * 导出计算预下市使用
	 */
	private String inFactoryCode;
	
	/**
	 * 订单中心行号（业务单据明细关联主键）
	 */
	private String orderLineNo;

}