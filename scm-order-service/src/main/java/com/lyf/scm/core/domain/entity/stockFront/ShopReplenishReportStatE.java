package com.lyf.scm.core.domain.entity.stockFront;

import lombok.Data;

/**
 * @Remarks
 * @date 2020/6/22
 */
@Data
public class ShopReplenishReportStatE {
    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 工厂名称
     */
    private String factoryName;

    /**
     * 仓库ID
     */
    private Long RealWarehouseId;

    /**
     * 仓库外部编码
     */
    private String realWarehouseOutCode;

    /**
     * 仓库编号
     */
    private String outRealWarehoseCode;

    /**
     * 仓库名称
     */
    private String outRealWarehouseName;

    /**
     * 采购单据类型
     */
    private Integer recordType;

    /**
     * 采购单据类型名称
     */
    private String recordTypeName;

    /**
     * 配货类型
     */
    private Integer requireType;

    /**
     * 配货类型
     */
    private String requireTypeName;

    /**
     * PO单数
     */
    private Integer poCount;

    /**
     * 中台出库单数
     */
    private Integer outRecordCount;

    /**
     * SAP交货单数
     */
    private Integer sapOrderCount;
}
