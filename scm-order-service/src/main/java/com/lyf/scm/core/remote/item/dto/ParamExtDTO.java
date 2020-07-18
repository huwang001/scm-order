package com.lyf.scm.core.remote.item.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParamExtDTO implements Serializable {

	/**
	 * skuId
	 */
    private Long skuId;

    /**
     * sku编码
     */
    private String skuCode;
    
    /**
     * 单位
     */
    private String  unitCode;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 商家id
     */
    private Long merchantId;

}