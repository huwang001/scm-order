package com.lyf.scm.core.domain.model.reverse;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;

import lombok.Data;

/**
 * @Description: 冲销单明细数据结构原始对象 <br>
 *
 * @Author wwh 2020/7/16
 */
@Data
public class ReverseDetailDO extends SkuQtyUnitBase implements Serializable {
	
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 单位名称
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 冲销数量
     */
    private BigDecimal reverseQty;

    /**
     * 实际出/入库数量
     */
    private BigDecimal actualQty;

    /**
     * 批次备注
     */
    private String batchRemark;

}