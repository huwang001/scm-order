package com.lyf.scm.core.domain.model.pack;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhanglong
 */
@Data
public class PackDemandDetailDO extends SkuQtyUnitBase implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 需求单code
     */
    private String recordCode;

    /**
     * 自定义组合码
     */
    private String customGroupCode;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String skuName;

    /**
     * 需求数量
     */
    private BigDecimal requireQty;

    /**
     * 组合份数量
     */
    private BigDecimal compositeQty;

    /**
     * 实际已包装数量
     */
    private BigDecimal actualPackedQty;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单位编号
     */
    private String unitCode;

    /**
     * 明细备注
     */
    private String remark;

}