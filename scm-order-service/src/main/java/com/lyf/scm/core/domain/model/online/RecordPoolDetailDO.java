package com.lyf.scm.core.domain.model.online;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 前置单据详情
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class RecordPoolDetailDO extends SkuQtyUnitBase implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 单据id
     */
    private Long recordPoolId;
    /**
     * 商品sku编码
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
     * 单位code
     */
    private String unitCode;
    /**
     * 实仓ID
     */
    private Long realWarehouseId;
    /**
     * 工厂编码
     */
    private String factoryCode;
    /**
     * 仓库编码
     */
    private String realWarehouseCode;
    /**
     * 虚仓ID
     */
    private Long virtualWarehouseId;
    /**
     * 虚仓编码
     */
    private String virtualWarehouseCode;
    /**
     * 基础单位的数量
     */
    private BigDecimal basicSkuQty;
    /**
     * 基础单位名称
     */
    private String basicUnit;
    /**
     * 基础单位code
     */
    private String basicUnitCode;
    /**
     * do单号
     */
    private String doCode;
    /**
     * 行号
     */
    private String lineNo;
}
