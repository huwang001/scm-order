package com.lyf.scm.core.api.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CancelReverseDTO {

    @ApiModelProperty(value = "需求单号")
    private String recordCode;

    @ApiModelProperty(value = "取消状态：0-成功，1-不允许取消")
    private Integer status;

    @ApiModelProperty(value = "不允许取消原因")
    private String message;
}
