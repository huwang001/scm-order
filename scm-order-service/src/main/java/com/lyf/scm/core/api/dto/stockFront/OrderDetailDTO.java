package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class OrderDetailDTO {
    @ApiModelProperty(value = "数量")
    @NotNull
    @Digits(integer = 9, fraction = 3,message="商品数量超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "sku编号")
    private Long skuId;

    @ApiModelProperty(value = "sku编号")
    @NotBlank
    private String skuCode;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    @NotBlank(message="单位code不能为空")
    private String unitCode;
}
