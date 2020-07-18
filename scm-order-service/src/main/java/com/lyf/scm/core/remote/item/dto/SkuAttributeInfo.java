package com.lyf.scm.core.remote.item.dto;

import com.lyf.scm.core.api.dto.BaseDTO;
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
    private Integer combineType;

    /**
     *sku名称
     */
    private String name;

    /**
     *sku编号
     */
    private String skuCode;

    /**
     *sku类型
     */
    private String skuType;

    /**
     *单位信息
     */
    private List<SkuUnitExtDTO> skuUnitExtDTO;

}
