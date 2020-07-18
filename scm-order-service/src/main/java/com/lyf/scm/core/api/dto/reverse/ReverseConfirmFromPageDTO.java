package com.lyf.scm.core.api.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Desc: 页面请求冲销单确认
 * @author:Huangyl
 * @date: 2020/7/17
 */
@Data
public class ReverseConfirmFromPageDTO {

    @ApiModelProperty(value = "操作人")
    private Long userId;

    @ApiModelProperty(value = "需求单号")
    private List<String> recordCodeList;
}
