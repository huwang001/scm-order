package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;
import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zys
 * @Description 调拨单详情
 * @date 2020/6/15 15:15
 * @Version
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ShopAllocationDetailDO extends SkuQtyUnitBase implements Serializable {
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
     * 商品skuId
     */
    private Long skuId;
    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     * 商品数量
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
