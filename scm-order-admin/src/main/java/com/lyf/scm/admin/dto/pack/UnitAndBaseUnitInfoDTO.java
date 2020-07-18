package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/9
 */
@Data
public class UnitAndBaseUnitInfoDTO {
    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "单位Id")
    private Long unitId;

    @ApiModelProperty(value = "单位Code")
    private String unitCode;

    @ApiModelProperty(value = "单位类型")
    private Long unitType;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "转换成基础单位的比例")
    private BigDecimal scale;

    @ApiModelProperty(value = "基础单位id")
    private Long basicUnitId;

    @ApiModelProperty(value = "基础单位code")
    private String basicUnitCode;

    @ApiModelProperty(value = "基础单位名称")
    private String basicUnitName;
}
