package com.lyf.scm.admin.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Description: OrderVwMoveDetailDTO
 * <p>
 * @Author: chuwenchao  2020/4/9
 */
@Data
public class OrderVwMoveDetailDTO {

    /**
     * 唯一主键
     */
    private Long id;

    @ApiModelProperty("商品编码")
    @NotBlank(message = "商品编码不能为空")
    private String skuCode;

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("下单数量")
    private BigDecimal orderQty;

    @ApiModelProperty("需求锁定数量")
    private BigDecimal requireQty;

    @ApiModelProperty("已锁定数量")
    private BigDecimal hasLockQty;

    @ApiModelProperty("虚仓库存数量")
    private BigDecimal vmStockQty;

    @ApiModelProperty("需要转移数量")
    private BigDecimal needMoveQty;

    @ApiModelProperty("转移数量")
    @NotNull(message = "转移数量不能为空")
    private BigDecimal moveQty;

    @ApiModelProperty("单位名称")
    @NotBlank(message = "单位名称不能为空")
    private String unit;

    @ApiModelProperty("单位编码")
    @NotBlank(message = "单位编码不能为空")
    private String unitCode;

}
