package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @date 2020/6/19
 */
@Data
@EqualsAndHashCode
public class ShopReplenishReportDetailDTO {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商品编号")
    private String skuCode;

    /**
     * 单位code
     */
    @ApiModelProperty(value = "单位code")
    private String unitCode;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品ID")
    private Long skuId;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;

    @ApiModelProperty(value = "工厂名称")
    private String factoryName;

    @ApiModelProperty(value = "仓库编号")
    private String outRealWarehoseCode;

    @ApiModelProperty(value = "仓库名称")
    private String outRealWarehouseName;

    @ApiModelProperty(value = "门店编号")
    private String inShopCode;

    @ApiModelProperty(value = "门店名称")
    private String inRealWarehouseName;

    @ApiModelProperty(value = "前置单号")
    private String recordCode;

    @ApiModelProperty(value = "前置类型名称")
    private String frontRecordTypeName;

    @ApiModelProperty(value = "SAP采购单号")
    private String sapPoNo;

    @ApiModelProperty(value = "SAP交货单号")
    private String sapOrderCode;

    @ApiModelProperty(value = "需求数量")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "寻源数量")
    private BigDecimal allotQty;

    @ApiModelProperty(value = "出库单锁定数量")
    private BigDecimal planQty;

    @ApiModelProperty(value = "实际出库数量")
    private BigDecimal realOutQty;

    @ApiModelProperty(value = "实际入库数量")
    private BigDecimal realInQty;

    @ApiModelProperty(value = "采购单据类型")
    private Integer recordType;

    @ApiModelProperty(value = "配货类型")
    private Integer requireType;

    private Date createTime;

    /**
     * 单据状态
     */
    private Integer recordStatus;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 收货时间
     */
    private Date receiverTime;

    /**
     * 采购单行号
     */
    private String lineNo;
}
