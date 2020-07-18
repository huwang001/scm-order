package com.lyf.scm.core.api.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Desc:根据SO创建需求单
 * @author:Huangyl
 * @date: 2020/7/8
 */
@Data
public class DemandFromSoDTO {

    @ApiModelProperty(value = "操作人")
    private Long userId;

    @ApiModelProperty(value = "预约单号")
    private String orderCode;
}
