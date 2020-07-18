package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.ShopAllocationDetailDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zys
 * @Description 门店调拨单详情
 * @date 2020/6/15 15:23
 * @Version
 */
@Data
public class ShopAllocationDetailE extends ShopAllocationDetailDO {

    /**
     * sku名称
     */
    private String skuName;


    /**
     * 单位比例关系
     */
    private BigDecimal scale;

    /**
     * 基础单位名称
     */
    private String unit;

    /**
     * 基础单位code
     */
    private String unitCode;
    /**
     * 商品批次
     */
    private List<FrontBatchStockE> batchStocks;
}
