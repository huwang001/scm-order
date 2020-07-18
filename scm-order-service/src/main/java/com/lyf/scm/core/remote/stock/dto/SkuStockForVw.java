package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品库存
 */
@Data
@EqualsAndHashCode
public class SkuStockForVw {
    /**
     * 虚仓ID
     */
    @ApiModelProperty(value = "虚仓ID")
    private Long virtualWarehouseId;
    @ApiModelProperty(value = "merchantId,不传将会用默认的商家id", name = "merchantId" )
    private Long merchantId;
    @ApiModelProperty(value = "skuCode")
    private String skuCode;
    @ApiModelProperty(value = "sku真实数量")
    private BigDecimal availableQty;
    @ApiModelProperty(value = "查询单位")
    private String unitCode;
}
