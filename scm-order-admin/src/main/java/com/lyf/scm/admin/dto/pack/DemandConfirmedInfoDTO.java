package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/9
 */
@Data
public class DemandConfirmedInfoDTO {

    @ApiModelProperty(value = "需求单号")
    private String recordCode;

    @ApiModelProperty(value = "状态0:成功，1:失败")
    private Integer status;

    @ApiModelProperty(value = "错误信息")
    private String message;
}
