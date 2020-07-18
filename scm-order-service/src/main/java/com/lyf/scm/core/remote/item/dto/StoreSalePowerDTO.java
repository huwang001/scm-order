package com.lyf.scm.core.remote.item.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 19:00
 * @Version
 */
@Data
public class StoreSalePowerDTO {
    @ApiModelProperty(value = "品牌id")
    private Integer brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "类目编码")
    private Long categoryCode;

    @ApiModelProperty(value = "类目id")
    private Long categoryId;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道sku简称")
    private String channelSkuAbbreviation;

    @ApiModelProperty(value = "渠道sku名称")
    private String channelSkuName;

    @ApiModelProperty(value = "渠道sku销售单位主键id")
    private Long channelSkuSaleUnitId;

    @ApiModelProperty(value = "运费模板id")
    private Long deliveryTemplateId;

    @ApiModelProperty(value = "运费模板名称")
    private String deliveryTemplateName;

    @ApiModelProperty(value = "毛重")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "高")
    private BigDecimal height;

    @ApiModelProperty(value = "是否上下架 0下架 1上架")
    private Integer isObtained;

    @ApiModelProperty(value = "长")
    private BigDecimal length;

    @ApiModelProperty(value = "净重")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "价格有效开始时间")
    private Date salePriceBeginTime;

    @ApiModelProperty(value = "价格有效结束时间")
    private Date salePriceEndTime;

    @ApiModelProperty(value = "销售单位code")
    private String saleUnitCode;

    @ApiModelProperty(value = "销售单位id")
    private Long saleUnitId;

    @ApiModelProperty(value = "销售单位名称")
    private String saleUnitName;

    @ApiModelProperty(value = "转换成基础单位的比例")
    private BigDecimal scale;

    @ApiModelProperty(value = "sku简称")
    private String skuAbbreviation;

    @ApiModelProperty(value = "sku条形码")
    private String skuBarCode;

    @ApiModelProperty(value = "sku主键id")
    private Long skuId;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "sku主图")
    private String skuPicture;

    @ApiModelProperty(value = "sku价格")
    private BigDecimal skuSalePrice;

    @ApiModelProperty(value = "sku类型编码")
    private String skuTypeCode;

    @ApiModelProperty(value = "sku类型id")
    private Long skuTypeId;

    @ApiModelProperty(value = "sku类型名称")
    private String skuTypeName;

    @ApiModelProperty(value = "宽")
    private BigDecimal width;

}
