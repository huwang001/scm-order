package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SalesReturnDetailDO extends SkuQtyUnitBase implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 所属单据编码
     */
    private String recordCode;

    /**
     * 单据id
     */
    private Long frontRecordId;

    /**
     * 商品skuID
     */
    private Long skuId;

    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     * 数量
     */
    private BigDecimal skuQty;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;
}