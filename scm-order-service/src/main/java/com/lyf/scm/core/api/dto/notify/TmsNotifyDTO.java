package com.lyf.scm.core.api.dto.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 派车单回调通知
 * <p>
 * @Author: huwang  2020/6/19
 */
@Data
public class TmsNotifyDTO implements Serializable {

    @ApiModelProperty(value = "出库单据编号")
    @NotBlank(message = "出库单据编号不能为空")
    private String recordCode;

    @ApiModelProperty(value = "TMS派车单号")
    private String tmsRecordCode;

    @ApiModelProperty(value = "单据类型")
    private Integer recordType;

    @ApiModelProperty(value = "是否需要派车 0否 2是")
    @NotNull(message = "是否需要派车不能为空")
    private Integer isDispatch;

}
