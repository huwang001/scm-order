package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批次信息
 * 
 * @author WWH
 *
 */
@Data
public class RwBatchDTO implements Serializable {

	@ApiModelProperty(value = "PO单行号")
	@NotBlank(message = "PO单行号不能为空")
	private String lineNo;

	@ApiModelProperty(value = "skuId")
	@NotNull(message = "skuId不能为空")
	private Long skuId;

	@ApiModelProperty(value = "sku编码")
	@NotBlank(message = "sku编码不能为空")
	private String skuCode;

	@ApiModelProperty(value = "实际出/入库数量")
	@NotNull(message = "实际出/入库数量不能为空")
	private BigDecimal actualQty;

}