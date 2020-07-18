package com.lyf.scm.admin.dto.pack;

import com.lyf.scm.admin.remote.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/8
 */
@Data
public class SkuParamDTO extends BaseDTO {

    /**
     * 商品code
     */
    @ApiModelProperty(value = "商品codeList")
    private List<String>  skuCodes;

    /**
     * 商品类型code
     */
    @ApiModelProperty(value = "商品类型code")
    private String skuType;

    /**
     * 商品类型名称
     */
    @ApiModelProperty(value = "商品类型名称")
    private String skuTypeName;
}
