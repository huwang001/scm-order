package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Remarks
 * @date 2020/7/2
 */
@Data
public class RecordPoolDetailDTO {
    /**
     * 商品sku编码
     */
    @ApiModelProperty(value = "skuId")
    private Long skuId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "sku数量")
    private BigDecimal skuQty;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "行号")
    private String lineNo;
}
