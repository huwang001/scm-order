package com.lyf.scm.core.remote.item.dto;

import lombok.Data;

/**
 * @author zys
 * @Description 类QuerySkuParamExtDTO的实现描述：查询sku参数
 * @date 2020/6/12 11:25
 * @Version
 */
@Data
public class QuerySkuParamExtDTO {

    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 商品Id
     */
    private Long skuId;

    /**
     * 商品code
     */
    private String skuCode;
}
