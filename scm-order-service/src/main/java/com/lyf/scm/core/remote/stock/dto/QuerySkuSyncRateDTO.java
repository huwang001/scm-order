package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * 查询SKU同步比例
 * 
 * @author WWH
 *
 */
@Data
public class QuerySkuSyncRateDTO {
	
    private Long skuId;
    
    /**
     * 实仓编码
     */
    private String warehouseOutCode;		  
	
    /**
     * 工厂编码
     */
    private String factoryCode;
    
	/**
	 * 虚仓编码
	 */
    private String virtualWarehouseCode;

}