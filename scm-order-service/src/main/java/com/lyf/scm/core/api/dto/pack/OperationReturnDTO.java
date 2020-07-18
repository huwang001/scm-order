package com.lyf.scm.core.api.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OperationReturnDTO implements Serializable {

    @ApiModelProperty(value = "任务操作单号")
    private String taskDetailOperateCode;

    @ApiModelProperty(value = "状态0:成功，1:失败")
    private Integer status;

    @ApiModelProperty(value = "错误信息")
    private String message;
}
