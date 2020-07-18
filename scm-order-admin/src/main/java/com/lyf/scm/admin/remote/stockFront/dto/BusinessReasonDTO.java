package com.lyf.scm.admin.remote.stockFront.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author zhangtuo
 * @Date 2019/5/13 15:07
 * @Version 1.0
 */
@Data
public class BusinessReasonDTO implements Serializable {

	@ApiModelProperty(value = "唯一主键")
	private Long id;

	@ApiModelProperty(value = "原因编号")
	private String reasonCode;

	@ApiModelProperty(value = "原因名称")
	private String reasonName;

	@ApiModelProperty(value = "单据类型")
	private Integer recordType;

}