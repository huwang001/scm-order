package com.lyf.scm.core.remote.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类SkuUnitParamExtDTO的实现描述：商品和单位查询dto
 *
 * @author sunyj 2019/5/23 16:19
 */
@Data
public class SkuUnitParamExtDTO {

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 单位
     */
    private String  unitCode;

    /**
     * merchantId
     */
    private Long  merchantId;

    /**
     * 单位名称
     */
    private String unitName;
    
}