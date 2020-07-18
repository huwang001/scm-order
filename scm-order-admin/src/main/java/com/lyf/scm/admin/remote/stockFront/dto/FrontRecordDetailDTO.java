package com.lyf.scm.admin.remote.stockFront.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class FrontRecordDetailDTO {
    /**
     * 数量
     */
    private Long skuQty;

    /**
     * sku编号
     */
    private Long skuId;

    /**
     * 单位
     */
    private Long unit;
}
