package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
/**
 * @Description 电商，旺店通下单明细入参
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@EqualsAndHashCode
public class OnlineOrderDetailDTO {

    @ApiModelProperty(value = "行号")
    private String lineNo;

    @ApiModelProperty(value = "数量")
    @NotNull
    @Digits(integer = 9, fraction = 3,message="商品数量超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "sku编号")
    private Long skuId;

    @ApiModelProperty(value = "sku编号")
    @NotBlank
    private String skuCode;

    @ApiModelProperty(value = "该sku所属主品的编码,如果不是组合品，该字段不传")
    private String parentSkuCode;

    @ApiModelProperty(value = "1赠品 2非赠品")
    private Integer giftType;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    @NotBlank(message="单位code不能为空")
    private String unitCode;
}
