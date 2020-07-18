package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description: 门店补货详情 <br>
 *
 * @Author chuwenchao 2020/6/10
 */
@Data
public class ReplenishDetailDO extends SkuQtyUnitBase {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据编码
     */
    private String recordCode;
    /**
     * 前置单据id
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

    /**
     * 分配数量
     */
    private BigDecimal allotQty;

    /**
     * 基础单位的数量
     */
    private BigDecimal leftBasicSkuQty;

    /**
     * 换算比例
     */
    private BigDecimal skuScale;

    /**
     * 实际出库数量
     */
    private BigDecimal realOutQty;

    /**
     * 实际入库数量
     */
    private BigDecimal realInQty;

    /**
     * sap采购单行号
     */
    private String lineNo;

    /**
     * sap po单号
     */
    private String sapPoNo;

}
