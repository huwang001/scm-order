package com.lyf.scm.core.remote.item.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 18:53
 * @Version
 */
@Data
public class StorePurchasePowerDTO {
    @ApiModelProperty(value = "sku主键id")
    private Long id;

    @ApiModelProperty(value = "是否有进货权0否1是")
    private String isAccess;

    @ApiModelProperty(value = "sku名称")
    private String name;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "门店编码")
    private String storeCode;
}
