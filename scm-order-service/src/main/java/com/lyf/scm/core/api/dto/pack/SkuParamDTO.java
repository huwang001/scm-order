package com.lyf.scm.core.api.dto.pack;

import com.lyf.scm.core.api.dto.BaseDTO;
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
    private List<String>  skuCodes;

    /**
     * 商品类型code
     */
    private String skuType;

    /**
     * 商品类型名称
     */
    private String skuTypeName;
}
