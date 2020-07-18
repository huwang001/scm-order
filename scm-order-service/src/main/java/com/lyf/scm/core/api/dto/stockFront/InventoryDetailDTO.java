package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 门店盘点单详情
 *
 * @author sunyj 2019/4/21 17:06
 */
@Data
@EqualsAndHashCode
public class InventoryDetailDTO {

    @ApiModelProperty(value = "实盘数量")
    @NotNull(message = "商品数量不能为空")
    @Range(min = 0, message = "商品不能为负数")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "skuID")
    private Long skuId;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "账面数量")
    private BigDecimal accQty;

    @ApiModelProperty(value = "差异数量")
    private BigDecimal diffQty;

    private String unit;

    private String unitCode;

}
