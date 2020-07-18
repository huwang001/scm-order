package com.lyf.scm.common.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pagination implements Serializable {
	@ApiModelProperty(value = "页面")
	private Integer pageIndex=1;
	@ApiModelProperty(value = "总数")
	private Long total;
	@ApiModelProperty(value = "页面大小")
	private Integer pageSize=10;
}
