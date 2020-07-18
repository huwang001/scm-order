package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.ShopInventoryDetailDO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopInventoryDetailE extends ShopInventoryDetailDO {

    private String skuName;

    /**
     * 库存账面数量
     */
    private BigDecimal stockQty;

    /**
     * 库存差异数量
     */
    private BigDecimal diffStockQty;
}
