package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class VwDetailDTO {
    /**
     * 商品code
     */
    @ApiModelProperty(value = "商品code")
    private String skuCode;
    /**
     * 商品计量单位
     */
    @ApiModelProperty(value = "单位code")
    private String skuUnitCode;
    /**
     * 商品转移数量
     */
    @ApiModelProperty(value = "商品转移数量")
    private BigDecimal skuQty;
}
