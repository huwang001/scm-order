package com.lyf.scm.admin.remote.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 公司部门dto
 * @Author zhangtuo
 * @Date 2019/6/19 15:34
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class OrganizationDTO {

	@ApiModelProperty(value = "组织id")
	private Long id;

	@ApiModelProperty(value = "组织编号")
	private String orgCode;

	@ApiModelProperty(value = "组织名称")
	private String orgName;

	@ApiModelProperty(value = "组织简称")
	private String orgShotName;

	@ApiModelProperty(value = "父级组织编号")
	private String parentOrgCode;

	@ApiModelProperty(value = "父级组织名称")
	private String parentOrgName;

	@ApiModelProperty(value = "")
	private String orgManager;

	@ApiModelProperty(value = "组织类型")
	private String orgType;

	@ApiModelProperty(value = "组织邮箱")
	private String orgEmail;

	@ApiModelProperty(value = "组织电话")
	private String orgTel;

	@ApiModelProperty(value = "组织子集")
	private List<OrganizationDTO> childrenNodes;

}
