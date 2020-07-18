package com.lyf.scm.core.remote.item.dto;

import lombok.Data;

/**
 * @Doc:查询门店所对应的的进货权限dto
 * @Author: lchy
 * @Date: 2019/11/5
 * @Version 1.0
 */
@Data
public class StorePurchaseAccessDTO {
	
    /**
     * 是否允许进货 00正常 01预下市 02下市
     */
    private String isAccess;
    
    /**
     * 物料号也就是skuCode
     */
    private String skuCode;

    private Long skuId;

    /**
     * 门店编号，经三方沟通，该字段实际上就是工厂编码
     */
    private String storeCode;

}