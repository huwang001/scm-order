package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;

import lombok.Data;

/**
 * @Description
 * @Author zhangtuo
 * @Date 2019/5/13 15:21
 * @Version 1.0
 */
@Data
public class BusinessReasonDO extends BaseDO {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 原因code
	 */
	private String reasonCode;

	/**
	 * 原因名称
	 */
	private String reasonName;

	/**
	 * 单据类型
	 */
	private Integer recordType;

}