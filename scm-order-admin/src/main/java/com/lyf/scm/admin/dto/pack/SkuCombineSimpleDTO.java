package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/9
 */
@Data
public class SkuCombineSimpleDTO {

    /**
     * 被组合sku编码-code
     */
    private String combineSkuCode;
    /**
     * 被组合sku名称-name
     */
    private String combineSkuName;
    /**
     * 被组合skuId
     */
    private Integer combineSkuId;
    /**
     * 被组合sku销售单位
     */
    private Integer combineSkuUnit;
    /**
     * 被组合sku销售单位Code
     */
    private String combineSkuUnitCode;
    /**
     * 组合sku主键id
     */
    private Integer id;
    /**
     * 组合数量(浮点型,因为可能是1.5个xx商品组合成一个xx商品)
     */
    private BigDecimal num;
    /**
     * 组合sku编码-code
     */
    private String skuCode;
    /**
     * 组合skuId
     */
    private Integer skuId;

    /**
     * 箱单位换算比率
     */
    @ApiModelProperty("箱单位换算比率")
    private BigDecimal boxUnitRate;

    /**
     * 运输单位名称
     */
    @ApiModelProperty("运输单位名称")
    private String transportUnitName;
}
