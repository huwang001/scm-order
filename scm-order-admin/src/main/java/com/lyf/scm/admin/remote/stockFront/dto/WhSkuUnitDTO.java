package com.lyf.scm.admin.remote.stockFront.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类WhSkuUnitDTO的实现描述：仓库调拨的单位
 *
 * @author sunyj 2019/5/29 13:45
 */
@Data
public class WhSkuUnitDTO {

	@ApiModelProperty(value = "sku主键")
	private Long skuId;

	@ApiModelProperty(value = "单位id")
	private Long unitId;

	@ApiModelProperty("单位编号")
	private String code;

	@ApiModelProperty(value = "换算比例")
	private BigDecimal scale;

	@ApiModelProperty(value = "单位code")
	private String unitCode;

	@ApiModelProperty(value = "单位名称")
	private String unitName;

	@ApiModelProperty(value = "1库存单位 2销售单位 3运输单位 4采购单位 5基础单位")
	private Long type;

}