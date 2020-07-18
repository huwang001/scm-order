package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * 查询库存中心WMS配置
 * 
 * @author WWH
 *
 */
@Data
public class QueryWmsConfigDTO {

	/**
	 * 仓库编码
	 */
	private String warehouseOutCode;

	/**
	 * 工厂编码
	 */
	private String factoryCode;

}