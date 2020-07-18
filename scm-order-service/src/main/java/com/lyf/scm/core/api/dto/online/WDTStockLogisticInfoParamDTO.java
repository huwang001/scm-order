package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 旺店通保存物流信息到so单入参
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
public class WDTStockLogisticInfoParamDTO implements Serializable {

    @ApiModelProperty(value = "订单编号", required = true)
    @NotBlank(message = "订单编号不能为空")
    private String soCode;


    @ApiModelProperty(value = "物流公司编码", required = true)
    @NotBlank(message = "物流公司编码不能为空")
    private String logisticsCode;
}
