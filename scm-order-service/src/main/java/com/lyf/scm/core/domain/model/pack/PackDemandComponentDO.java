package com.lyf.scm.core.domain.model.pack;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PackDemandComponentDO extends SkuQtyUnitBase implements Serializable {

    private static final long serialVersionUID = 7232322602852462580L;

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 需求单code
     */
    private String recordCode;

    /**
     * 成品商品编码
     */
    private String parentSkuCode;

    /**
     * 组件编号：自定义组合/反拆需要必填、组装反拆自动带入
     */
    private String skuCode;

    /**
     * BOM数量=需求数量/成品数量
     */
    private BigDecimal bomQty;

    /**
     * 需求数量
     */
    private BigDecimal requireQty;

    /**
     * 实际移库数量：后续调拨单进行更新
     */
    private BigDecimal actualMoveQty;

    /**
     * 需求箱单位数量=需求数量/箱单位换算率
     */
    private BigDecimal requireBoxQty;

    /**
     * 箱单位换算比率
     */
    private BigDecimal boxUnitRate;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 明细备注
     */
    private String remark;

    /**
     * 移库类型：1出库 2入库
     */
    private Integer moveType;

    /**
     * 是否领料 :1 是 0 否
     * 默认是1
     */
    private Boolean isPick = true;
}