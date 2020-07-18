package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Remarks
 * @date 2020/6/22
 */
@Data
@EqualsAndHashCode
public class ShopReplenishReportStatDTO implements Serializable {
    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;

    @ApiModelProperty(value = "工厂名称")
    private String factoryName;

    @ApiModelProperty(value = "仓库ID")
    private Long outRealWarehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String outRealWarehoseCode;

    @ApiModelProperty(value = "仓库名称")
    private String outRealWarehouseName;

    @ApiModelProperty(value = "采购单据类型")
    private Integer recordType;

    @ApiModelProperty(value = "采购单据类型")
    private String recordTypeName;

    @ApiModelProperty(value = "配货类型")
    private Integer requireType;

    @ApiModelProperty(value = "配货类型")
    private String requireTypeName;

    @ApiModelProperty(value = "PO单数")
    private Integer poCount;

    @ApiModelProperty(value = "中台出库单数")
    private Integer outRecordCount;

    @ApiModelProperty(value = "SAP交货单数")
    private Integer sapOrderCount;
}
