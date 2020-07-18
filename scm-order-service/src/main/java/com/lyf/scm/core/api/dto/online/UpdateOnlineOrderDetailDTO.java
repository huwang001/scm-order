package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 旺店通根据订单号取消so明细入参
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@EqualsAndHashCode
public class UpdateOnlineOrderDetailDTO implements Serializable {
    @NotBlank
    @ApiModelProperty(value = "行号", required = true)
    private String lineNo;

}
