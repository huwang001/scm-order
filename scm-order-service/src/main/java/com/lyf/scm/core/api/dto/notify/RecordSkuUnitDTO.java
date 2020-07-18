package com.lyf.scm.core.api.dto.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 出入库单据明细Sku单位集合
 * <p>
 * @Author: chuwenchao  2020/6/22
 */
@Data
public class RecordSkuUnitDTO {

    /**
     * 单位类型  发货（运输）单位：3  销售单位：2  基础单位：5
     */
    @ApiModelProperty(value = "单位类型  发货（运输）单位：3  销售单位：2  基础单位：5")
    private Integer unitType;

    /**
     * 单位编码
     */
    @ApiModelProperty(value = "单位编码")
    private String unitCode;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 单位转换比例
     */
    @ApiModelProperty(value = "单位转换比例")
    private BigDecimal scale;
}
