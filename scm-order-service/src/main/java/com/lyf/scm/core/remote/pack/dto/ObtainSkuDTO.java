package com.lyf.scm.core.remote.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Desc:组合、反拆时的sku列表信息
 * @author:Huangyl
 * @date: 2020/7/7
 */
@Data
public class ObtainSkuDTO {

    @ApiModelProperty("组件商品编码")
    private String bomSkuCode;
    @ApiModelProperty("组件商品名称")
    private String bomSkuName;
    @ApiModelProperty("BOM数量")
    private BigDecimal bomAmount;
    @ApiModelProperty("需求数量")
    private BigDecimal needAmount;
    @ApiModelProperty("单位")
    private String unit;
}
