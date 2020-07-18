package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.AdjustForetasteDetailDO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdjustForetasteDetailE extends AdjustForetasteDetailDO {

    private String skuName;

    /**
     * 实际出库数量
     */
    private BigDecimal actualQty;

    /**
     * 转换比例
     */
    private BigDecimal scale;
}
