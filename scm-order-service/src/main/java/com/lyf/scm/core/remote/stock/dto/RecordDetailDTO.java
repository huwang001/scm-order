package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecordDetailDTO {

    /**
     * 基础单位数量
     */
    private BigDecimal basicSkuQty;

    /**
     * 基础单位
     */
    private String basicUnit;

    /**
     * 基础单位编码
     */
    private String basicUnitCode;

    /**
     * 交货行号,供应链行号
     */
    private String deliveryLineNo;

    /**
     * sap行号
     */
    private String lineNo;

    /**
     * skuCode
     */
    private String skuCode;

    /**
     *  SAP的po单号
     */
    private String sapPoNo;
}
