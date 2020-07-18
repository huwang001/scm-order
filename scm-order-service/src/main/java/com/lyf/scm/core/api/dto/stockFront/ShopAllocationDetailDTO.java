package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 门店调拨单详情
 * @date 2020/6/15
 * @Version
 */
@Data
@EqualsAndHashCode
public class ShopAllocationDetailDTO extends BaseDTO implements Serializable {


    @ApiModelProperty(value = "调拨数量")
    @NotNull(message="商品数量不能为空")
    @Range(min=0, message = "商品不能为负数")
    private BigDecimal skuQty;

    /**
     * 商品名称
     */
    private String skuName;

    @ApiModelProperty(value = "sku编号")
    private Long skuId;

    /**
     * 商品code
     */
    private String skuCode;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "商品批次明细")
    private List<BatchStockDTO> batchStocks;

    @ApiModelProperty(value = "单位编码")
    private String unitCode;
}
