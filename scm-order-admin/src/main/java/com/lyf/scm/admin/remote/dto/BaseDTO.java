package com.lyf.scm.admin.remote.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 公共DTO字段
 *               <p>
 * @Author: chuwenchao 2020/3/6
 */
@Data
public class BaseDTO implements Serializable {

	@ApiModelProperty(value = "当前页码")
	private Integer pageNum;

	@ApiModelProperty(value = "每页记录数")
	private Integer pageSize;

	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "修改时间")
	private Date updateTime;

	@ApiModelProperty(value = "页面")
	private Integer pageIndex = 1;

	@ApiModelProperty(value = "总数")
	private Long total;

}