package com.lyf.scm.core.api.dto.stockFront;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类ShopTradeDetail的实现描述：门店交易明细
 */
@Data
@EqualsAndHashCode
public class ShopSaleDetailDTO {

    @ApiModelProperty(value = "数量")
    @NotNull(message = "商品数量不能为空")
    @Digits(integer = 9, fraction = 3, message = "超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "sku编号")
    @JsonIgnore
    private Long skuId;

    @ApiModelProperty(value = "sku编号")
    @NotBlank(message = "sku编码为空")
    private String skuCode;

    @ApiModelProperty(value = "单位")
    @NotEmpty(message = "sku单位不能为空")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    @NotEmpty(message = "单位code不能为空")
    private String unitCode;
}
