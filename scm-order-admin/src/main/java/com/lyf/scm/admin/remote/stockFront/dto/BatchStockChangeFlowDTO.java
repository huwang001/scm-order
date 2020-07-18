package com.lyf.scm.admin.remote.stockFront.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 门店零售详情页需要查看批次信息
 *
 * @author lyf
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
     * 真实库存
     */
    private Long skuQty;

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
