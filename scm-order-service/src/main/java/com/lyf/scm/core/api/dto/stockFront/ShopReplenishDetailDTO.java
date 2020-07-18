package com.lyf.scm.core.api.dto.stockFront;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 类ShopReplenishDetailDTO的实现描述：补货详情
 *
 * @author sunyj 2019/4/21 21:05
 */
@Data
@EqualsAndHashCode
public class ShopReplenishDetailDTO {

    @ApiModelProperty(value = "数量")
    @NotNull(message="明细数量不能为空")
    @Digits(integer = 9, fraction = 3,message="商品数量超过范围,小数3位有效位，整数9位有效位")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "sku编号")
    @JsonIgnore
    private Long skuId;

    @ApiModelProperty(value = "skuCode")
    @NotBlank(message="明细skuCode不能为空")
    private String skuCode;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    @NotBlank(message="单位code不能为空")
    private String unitCode;

    @ApiModelProperty(value = "是否逻辑删除 0-否，1-是")
    private Byte isDeleted;

    @ApiModelProperty(value = "sap行号")
    private String lineNo;

    /**
     * sap po单号
     */
    private String sapPoNo;
}
