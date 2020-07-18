package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 旺店通根据订单号取消so明细入参
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@EqualsAndHashCode
public class UpdateOnlineSoOrderDTO implements Serializable {
    @ApiModelProperty(value = "so单号", required = true)
    @NotBlank
    private String soCode;

    @Valid
    private List<UpdateOnlineOrderDetailDTO> frontRecordDetails;
}
