package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/9
 */
@Data
public class DemandConfirmFromPageDTO {

    @ApiModelProperty(value = "操作人")
    private Long userId;

    @ApiModelProperty(value = "需求单号")
    private List<String> recordCodeList;
}
