package com.lyf.scm.admin.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 11:05
 * @Version
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
