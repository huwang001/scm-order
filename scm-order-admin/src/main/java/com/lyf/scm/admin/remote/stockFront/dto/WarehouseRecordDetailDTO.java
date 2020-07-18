package com.lyf.scm.admin.remote.stockFront.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出入单详情
 * @author sunyj 2019/4/23 20:29
 */
@Data
@EqualsAndHashCode
public class WarehouseRecordDetailDTO {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 商品sku编码
     */
    private Long skuId;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 计划出/入库数量
     * */
    private Long planQty;

    /**
     * 实际出/入库数量
     * */
    private Long actualQty;

    /**
     * 计件单位
     */
    private String unit;

    /**
     * 计件单位
     */
    private String unitCode;
    
    /**
     * sap采购单号
     */
    private String sapPoNo;

    /**
     * 采购单行号
     */
    private String lineNo;

    /**
     * 交货单行号
     * */
    private String deliveryLineNo;

}
