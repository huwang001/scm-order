package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

@Data
public class BaseSkuInfoDTO {

    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     * 商品skuId
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 单位名称
     */
    private String unit;

    /**
     * 单位code
     */
    private String unitCode;
}
