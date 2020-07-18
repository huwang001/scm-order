package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description
 * @Author jinlei
 * @Date 2019/5/13 15:07
 * @Version 1.0
 */
@Data
public class BatchStockDTO implements Serializable {

	@ApiModelProperty(value = "商品skuID")
	private Long skuId;

	@ApiModelProperty(value = "商品编号")
	private String skuCode;

	@ApiModelProperty(value = "商品数量")
	private BigDecimal skuQty;

	@ApiModelProperty(value = "单位")
	private String unit;

	@ApiModelProperty(value = "单位编号")
	private String unitCode;

	@ApiModelProperty(value = "批次编码-批次号")
	private String batchCode;
	
}