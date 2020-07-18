package com.lyf.scm.core.remote.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description
 * @Author zhangtuo
 * @Date 2019/6/24 14:35
 * @Version 1.0
 */
@ApiModel(description="销售组织信息")
@Data
@EqualsAndHashCode
public class SaleOrgDTO {

    @ApiModelProperty(value = "组织code")
    private String orgCode;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "唯一id")
    private Long id;

}
