package com.lyf.scm.core.api.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Desc:页面请求需求单确认
 * @author:Huangyl
 * @date: 2020/7/8
 */
@Data
public class DemandConfirmFromPageDTO {

    @ApiModelProperty(value = "操作人")
    private Long userId;

    @ApiModelProperty(value = "需求单号")
    private List<String> recordCodeList;
}
