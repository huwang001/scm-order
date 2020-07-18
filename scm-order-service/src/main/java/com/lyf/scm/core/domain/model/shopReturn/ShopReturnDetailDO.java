package com.lyf.scm.core.domain.model.shopReturn;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopReturnDetailDO extends SkuQtyUnitBase {
    /**
     * 主键
     */
    private Long id;

    /**
     * 所属单据编码
     */
    private String recordCode;

    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     * po行号
     */
    private String lineNo;

    /**
     * 应退数量
     */
    private BigDecimal skuQty;

    /**
     * 实退数量
     */
    private BigDecimal realRefundQty;

    /**
     * 实收数量
     */
    private BigDecimal realEnterQty;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 含加成率价格
     */
    private BigDecimal additionRatePrice;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 退货原因
     */
    private String reason;
}