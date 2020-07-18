package com.lyf.scm.admin.remote.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Description
 * @date 2020/6/18
 * @Version
 */
@Data
@EqualsAndHashCode
public class ShopAllocationDetailDTO {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long skuId;

    /**
     * 商品编号
     */
    private String skuCode;

    /**
     * 实盘商品数量
     */
    private BigDecimal skuQty;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 单位
     */
    private String unit;
}
