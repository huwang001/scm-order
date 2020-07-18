package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * 查询库存中心虚仓信息
 * 
 * @author WWH
 *
 */
@Data
public class QueryVirtualWarehouseDTO {

	/**
	 * 工厂编码
	 */
	private String factoryCode;

	/**
	 * 仓库外部编码
	 */
	private String realWarehouseOutCode;
}
