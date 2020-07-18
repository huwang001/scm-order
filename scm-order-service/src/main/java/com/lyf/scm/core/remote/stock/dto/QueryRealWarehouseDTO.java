package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询库存中心实仓信息
 * 
 * @author WWH
 *
 */
@Data
@EqualsAndHashCode
public class QueryRealWarehouseDTO {

	/**
	 * 工厂编码
	 */
	private String factoryCode;

	/**
	 * 仓库外部编码
	 */
	private String warehouseOutCode;
}
