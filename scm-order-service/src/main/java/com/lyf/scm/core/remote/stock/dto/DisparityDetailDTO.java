package com.lyf.scm.core.remote.stock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Doc:.....
 * @Author: lchy
 * @Date: 2020/7/10
 * @Version 1.0
 */
@Data
public class DisparityDetailDTO {

    @ApiModelProperty(value = "行号，传差异单明细表的主键id，唯一", required = true)
    private Long lineNo;

    @ApiModelProperty(value = "sap内采单号", required = true)
    private String sapPoNo;

    @ApiModelProperty(value = "sap内采单行项目", required = true)
    private String sapLineNo;

    @ApiModelProperty(value = "物料编码", required = true)
    private String skuCode;


    @ApiModelProperty(value = "差异数量,即出入库数量[基本单位]", required = true)
    private BigDecimal diffQty;

    @ApiModelProperty(value = "基本单位", required = true)
    private String unitCode;


    @ApiModelProperty(value = "成本中心,[物流责任必填]")
    private String costCenter;

    @ApiModelProperty(value = "备注：承运商信息[物流责任必填]")
    private String remark;

    @ApiModelProperty(value = "备用字段")
    private String text1;
    @ApiModelProperty(value = "备用字段")
    private String text2;
    @ApiModelProperty(value = "备用字段")
    private String text3;
    @ApiModelProperty(value = "备用字段")
    private String text4;
    @ApiModelProperty(value = "备用字段")
    private String text5;
    @ApiModelProperty(value = "备用字段")
    private String text6;
    @ApiModelProperty(value = "备用字段")
    private String text7;

}
