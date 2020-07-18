package com.lyf.scm.core.remote.stock.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类SkuInfo的实现描述：sku信息
 *
 * @author sunyj 2019/4/16 9:51
 */
@Data
@EqualsAndHashCode
public class SkuInfoForVw {

    /**
     * 虚仓ID
     */
    @ApiModelProperty(value = "虚仓ID", name = "virtualWarehouseId", required=true)
    @NotBlank(message="虚仓ID")
    private Long virtualWarehouseId;

    /**
     * 虚仓Code
     */
    @ApiModelProperty(value = "虚仓ID", name = "virtualWarehouseCode", required=true)
    @NotBlank(message="虚仓ID")
    private String virtualWarehouseCode;


    @ApiModelProperty(value = "merchantId,不传将会用默认的商家id", name = "merchantId" )
    private Long merchantId;

    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "商品skuId", name = "skuId")
    @JsonIgnore
    private Long skuId;

    /**
     * 商品sku编码
     */
    @ApiModelProperty(value = "商品sku编码", name = "skuCode", required=true)
    private String skuCode;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位code", name = "unit", required = true)
    @NotBlank(message="单位编码不能为空")
    private String unitCode;


    /**
     *   sku类型 0单sku，1组合sku，2组装sku
     */
    @JsonIgnore
    private Integer combineType;

}
