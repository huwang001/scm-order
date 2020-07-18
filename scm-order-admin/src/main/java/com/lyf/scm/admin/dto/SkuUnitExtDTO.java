package com.lyf.scm.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/10
 */
@Data
public class SkuUnitExtDTO {
    /** 条码 */
    private String barCode;

    /** 基础单位转换比 */
    private BigDecimal basicScale;

    /** 基础单位code */
    private String basicUnitCode;

    /** 基础单位id */
    private Long basicUnitId;

    /** 基础单位名称 */
    private String basicUnitName;

    /** 0普通 1组合 2组装 */
    private Integer combineType;

    /** 毛重 */
    private BigDecimal grossWeight;

    /** 高 */
    private BigDecimal height;

    /** 长 */
    private BigDecimal length;

    /** 商家id */
    private Long merchantId;

    /** 商家名称 */
    private String merchantName;

    /** 净重 */
    private BigDecimal netWeight;

    /** 转换成基础单位的比例 */
    private BigDecimal scale;

    /** sku编码 */
    private String skuCode;

    /** sku主键Id */
    private Long skuId;

    /** 类型 */
    private Long type;

    /** 单位Code */
    private String unitCode;

    /** 单位Id */
    private Long unitId;

    /** 单位名称 */
    private String unitName;

    /** 宽 */
    private BigDecimal width;

    /** 体积 */
    private BigDecimal volume;

    public String getSkuTypePrimaryKey(){
        return this.skuCode+"_"+this.type;
    }

    public String getSkuUnitCodeByPrimaryKey(){
        return this.skuCode+"_"+this.unitCode;
    }

}
