package com.lyf.scm.core.api.dto.shopReturn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/14
 */
@Data
public class ShopReturnDetailDTO {

    @ApiModelProperty(value = "数量")
    @NotNull(message = "商品数量不能为空")
    @Digits(integer = 9, fraction = 3, message = "超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "含加成率价格")
    @Digits(integer = 9, fraction = 3, message = "超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal additionRatePrice;

    @ApiModelProperty(value = "总金额")
    @Digits(integer = 9, fraction = 3, message = "超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "sku编号")
    @NotBlank(message = "sku编码为空")
    private String skuCode;

    @ApiModelProperty(value = "单位")
    @NotEmpty(message = "sku单位不能为空")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    @NotEmpty(message = "单位code不能为空")
    private String unitCode;

    @ApiModelProperty(value = "退货原因")
    private String reason;

    @ApiModelProperty(value = "sap行号")
    @NotBlank(message = "sap行号不能为空")
    private String lineNo;

    /**
     * 装载组类型
     */
    private Integer loadType;
}
