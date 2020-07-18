package com.lyf.scm.core.domain.model.pack;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PackTaskFinishDetailDO extends SkuQtyUnitBase implements Serializable {

    /**
     * 唯一主键
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

    /**
     * 备注
     */
    private String remark;
}