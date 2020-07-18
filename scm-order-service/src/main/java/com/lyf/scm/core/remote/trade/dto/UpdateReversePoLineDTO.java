package com.lyf.scm.core.remote.trade.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@ApiModel(description = "门店退货单-交易中心更新单据明细")
@Data
@EqualsAndHashCode
public class UpdateReversePoLineDTO {

    @ApiModelProperty("退货单号，非空")
    @NotBlank(message = "退货单号不能为空")
    private String reversePoNo;

    @ApiModelProperty("行号")
    @NotBlank(message = "行号不能为空")
    private String lineNo;

    @ApiModelProperty("责任方")
    private String mode;

    @ApiModelProperty(value = "退货数量")
    @NotBlank(message = "退货数量不能为空")
    private BigDecimal actualDeliveryQuantity;

    @ApiModelProperty(value = "收货数量")
    @NotBlank(message = "收货数量不能为空")
    private BigDecimal actualReceiveQuantity;

    @ApiModelProperty(value = "预退货数量")
    private BigDecimal predictDeliveryQuantity;

    @ApiModelProperty(value = "销售单位")
    @NotBlank(message = "销售单位不能为空")
    private String saleUnitCode;

    @ApiModelProperty(value = "sku编号")
    @NotBlank(message = "sku编号不能为空")
    private String skuCode;

    @ApiModelProperty(value = "sku唯一标识")
    private Long skuId;
}
