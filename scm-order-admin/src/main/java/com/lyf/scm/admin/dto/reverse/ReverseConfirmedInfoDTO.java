package com.lyf.scm.admin.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Desc:冲销单确认信息返回
 * @author:Huangyl
 * @date: 2020/7/17
 */
@Data
public class ReverseConfirmedInfoDTO {

    @ApiModelProperty(value = "冲销单号")
    private String recordCode;

    @ApiModelProperty(value = "状态0:成功，1:失败")
    private Integer status;

    @ApiModelProperty(value = "错误信息")
    private String message = "确认成功";
}
