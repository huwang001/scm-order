package com.lyf.scm.core.remote.item.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 18:51
 * @Version
 */
@Data
public class StorePurchaseDTO {

    @ApiModelProperty(value = "门店编码")
    private String  storeCode;

    @ApiModelProperty(value = "门店进货权集合")
    private List<StorePurchasePowerDTO> storeAccessList;
}
