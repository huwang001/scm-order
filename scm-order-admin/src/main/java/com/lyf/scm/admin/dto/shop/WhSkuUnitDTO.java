package com.lyf.scm.admin.dto.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zys
 * @Description 仓库调拨的单位
 * @date 2020/6/15 10:57
 * @Version
 */
@Data
public class WhSkuUnitDTO {
    @ApiModelProperty(value = "sku主键")
    private Long skuId;

    @ApiModelProperty(value = "单位id")
    private Long unitId;

    @ApiModelProperty("单位编号")
    private String code;

    @ApiModelProperty(value = "换算比例")
    private BigDecimal scale;

    @ApiModelProperty(value = "单位code")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 1: 库存单位 2:销售单位  3:运输单位 4:采购单位 5:基础单位
     */
    private Long type;
}
