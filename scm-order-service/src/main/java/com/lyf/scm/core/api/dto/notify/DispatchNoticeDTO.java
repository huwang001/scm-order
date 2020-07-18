package com.lyf.scm.core.api.dto.notify;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DispatchNoticeDTO {

    @ApiModelProperty(value = "派车类型")
    private Integer dispatchType;

    @ApiModelProperty(value = "是否需要派车 false否 true是")
    private Boolean isDispatch;

    @ApiModelProperty(value = "单据编码")
    private String recordCode;

    @ApiModelProperty(value = "系统来源")
    private String sourceSystem;

    @ApiModelProperty(value = "TMS派车单号")
    private String thirdRecordCode;
}
