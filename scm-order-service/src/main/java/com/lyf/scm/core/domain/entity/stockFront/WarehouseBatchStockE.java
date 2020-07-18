package com.lyf.scm.core.domain.entity.stockFront;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @date 2020/6/13
 * @Version
 */
@Data
public class WarehouseBatchStockE {

    /**
     * 商品skuID
     */
    private Long skuId;

    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     * 商品数量
     */
    private BigDecimal skuQty;

    /**
     * 实际单位名称
     */
    private String unit;
    /**
     * 实际单位code
     */
    private String unitCode;

    /**
     * 批次编码-批次号
     */
    private String batchCode;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 单据类型
     */
    private Integer recordType;

    /**
     * 1.出库 2.入库
     */
    private Integer stockType;

    /**
     * 实体仓库id
     */
    private Long realWarehouseId;
}
