package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class ShopAdjustDetailDTO {

    @ApiModelProperty(value = "数量")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "sku编号")
    private Long skuId;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "所属单据编码")
    private String recordCode;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    @NotEmpty(message = "单位code不能为空")
    private String unitCode;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "商品规格")
    private String skuStandard;

    @ApiModelProperty(value = "基本单位数量")
    private BigDecimal basicQty;

    @ApiModelProperty("基本单位")
    private String basicUnit;

    @ApiModelProperty(value = "实际出库数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "剩余数量")
    private BigDecimal remindQty;
}
