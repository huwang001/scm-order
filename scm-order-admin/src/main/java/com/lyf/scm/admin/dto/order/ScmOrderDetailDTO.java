package com.lyf.scm.admin.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 对外接口预约单明细DTO对象
 *
 * @author zhangxu
 * @date 2020/4/8
 */
@Data
@ApiModel(value = "预约单sku明细")
public class ScmOrderDetailDTO {

    @ApiModelProperty(value = "商品编号")
    @NotBlank(message = "商品编号不能为空")
    private String skuCode;

    @ApiModelProperty(value = "下单数量")
    @DecimalMin(value = "0", message = "下单数量不能为0")
    private BigDecimal orderQty;

    @ApiModelProperty(value = "单位编号")
    @NotBlank(message = "单位编号不能为空")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    @NotBlank(message = "单位名称不能为空")
    private String unit;

}
