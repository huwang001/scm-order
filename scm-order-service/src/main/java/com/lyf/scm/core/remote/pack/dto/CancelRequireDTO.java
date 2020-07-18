package com.lyf.scm.core.remote.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CancelRequireDTO {

    @ApiModelProperty(value = "需求单号")
    private String requireCode;

    @ApiModelProperty(value = "取消状态：0-成功，1-不允许取消")
    private Integer status;

    @ApiModelProperty(value = "不允许取消原因")
    private String message;
}
