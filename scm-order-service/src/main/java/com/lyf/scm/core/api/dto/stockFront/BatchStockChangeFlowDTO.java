package com.lyf.scm.core.api.dto.stockFront;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 批次流水
 *
 * @author sunyj
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class BatchStockChangeFlowDTO {
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 商品sku编码
     */
    private Long skuId;

    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     * 真实库存
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
     * 实体仓库id
     */
    private Long realWarehouseId;

    /**
     * 1.出库 2.入库
     */
    private Integer stockType;


    /**
     * 单据类型
     */
    private Integer recordType;

    /**
     * 单据编号
     */
    private String recordCode;


}
