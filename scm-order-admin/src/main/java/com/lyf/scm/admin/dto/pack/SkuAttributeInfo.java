package com.lyf.scm.admin.dto.pack;

import com.lyf.scm.admin.dto.SkuUnitExtDTO;
import com.lyf.scm.admin.remote.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/10
 */
@Data
public class SkuAttributeInfo extends BaseDTO {

    /**
     *sku组合类型
     */
    @ApiModelProperty(value = "sku组合类型")
    private Integer combineType;

    /**
     *sku名称
     */
    @ApiModelProperty(value = "sku名称")
    private String name;

    /**
     *sku编号
     */
    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    /**
     *sku类型
     */
    @ApiModelProperty(value = "sku类型")
    private String skuType;

    /**
     *单位信息
     */
    @ApiModelProperty(value = "单位信息")
    private List<SkuUnitExtDTO> skuUnitExtDTO;
}
