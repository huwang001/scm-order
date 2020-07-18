package com.lyf.scm.core.api.dto.stockFront;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Doc:门店零售出库单详情
 * @Author: lchy
 * @Date: 2019/5/29
 * @Version 1.0
 */
@Data
@EqualsAndHashCode
public class SaleWarehouseRecordDetailDTO {
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 所属单据编码
     */
    private String recordCode;

    /**
     * 所属单据id
     */
    private Long warehouseRecordId;

    /**
     * 商品sku编码
     */
    private Long skuId;

    /**
     * 商品skuCode
     */
    private String skuCode;

    /**
     * 商品数量
     */
    private BigDecimal planQty;

    /**
     * 基本计件单位名称
     */
    private String unit;

    /**
     * 基本计件单位
     */
    private String unitCode;
    /**
     * 实仓ID
     */
    private Long realWarehouseId;

    /**
     * 实仓ID
     */
    private String realWarehouseName;

    /**
     * 实际收货数量 包括待质检的
     */
    private BigDecimal actualQty;

    private String channelCode;


    private String skuName;


    private String code;

    /**
     * 类目名称
     */
    private String categoryName;

    /**
     * 类目编码Code
     */
    private Long categoryCode;

    /**
     * 行号
     */
    private String lineNo;

    private Date createTime;

    /**
     * 批次流水信息【门店销售查看sku批次信息】
     */
    private List<BatchStockChangeFlowDTO> batchStockChangeFlowList;

}
