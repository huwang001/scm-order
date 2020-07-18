package com.lyf.scm.core.remote.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class SkuQtyUnitDTO extends SkuQtyUnitBase {

    /**
     * 商品sku编码
     */
    private String channelCode;

}