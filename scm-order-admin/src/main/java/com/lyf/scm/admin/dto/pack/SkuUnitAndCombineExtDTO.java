package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/9
 */
@Data
public class SkuUnitAndCombineExtDTO {

    /**
     * sku类型 0单sku，1组合sku，2组装sku
     */
    @ApiModelProperty(value = "sku类型 0单sku，1组合sku，2组装sku")
    private Integer combineType;
    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Integer merchantId;
    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    private String skuCode;
    /**
     * sku主键id
     */
    @ApiModelProperty(value = "sku主键id")
    private Integer skuId;
    /**
     * sku名称
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    /**
     * 单位信息
     */
    @ApiModelProperty(value = "单位信息")
    private List<UnitAndBaseUnitInfoDTO> unitInfo;
    /**
     * 组合品信息
     */
    @ApiModelProperty(value = "组合品信息")
    private List<SkuCombineSimpleDTO> combineInfo;
}
