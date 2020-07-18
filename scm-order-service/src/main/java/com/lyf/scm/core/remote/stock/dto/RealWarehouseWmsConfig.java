package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**
 * 实仓与wms配置
 */
@Data
public class RealWarehouseWmsConfig {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
     * 实体仓库id
     */
    private Long realWarehouseId;

	/**
	 * 实体仓库编码
	 */
	private String realWarehouseCode;

	/**
	 * 工厂编码
	 */
	private String factoryCode;

	/**
	 * wms 编码
	 */
	private Integer wmsCode;

	/**
	 * wms名称
	 */
	private String wmsName;

	/**
	 * 实体仓库编码、工厂编码组合Key
	 * 
	 * @return
	 */
	public String getKey() {
		return this.realWarehouseCode + "_" + this.factoryCode;
	}

}