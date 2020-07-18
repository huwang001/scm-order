package com.lyf.scm.core.remote.item.dto;

import lombok.Data;
import java.math.BigDecimal;

import com.lyf.scm.core.domain.model.common.BaseDO;

@Data
public class SkuQtyUnitBase extends BaseDO {

    /**
     * 商品skuID
     */
    private Long skuId;
    
    /**
     * 商品sku编码
     */
    private String skuCode;
    
    /**
     * 实际单位的数量
     */
    private BigDecimal skuQty;
    
    /**
     * 实际单位名称
     */
    private String unit;
    
    /**
     * 实际单位code
     */
    private String unitCode;
    
    /**
     * 单位比例关系
     */
    private BigDecimal scale;
    
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
     * 集装箱需求 01整箱 02拆零
     */
    private String container;
    
    public String getKey(){
		return this.skuCode +"_"+ this.unitCode;
	}
    
}