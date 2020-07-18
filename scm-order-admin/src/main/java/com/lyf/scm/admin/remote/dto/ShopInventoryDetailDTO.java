package com.lyf.scm.admin.remote.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 门店盘点详情
 *
 * @author sunyj 2019/4/23 20:29
 */
@Data
@EqualsAndHashCode
public class ShopInventoryDetailDTO {

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

    /**
     * 账面数量
     */
    private BigDecimal accQty;

    /**
     * 差异数量
     */
    private BigDecimal diffQty;

    /**
     * 库存账面数量
     */
    private BigDecimal stockQty;

    /**
     * 库存差异数量
     */
    private BigDecimal diffStockQty;

}
