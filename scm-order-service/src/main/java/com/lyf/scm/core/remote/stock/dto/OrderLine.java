package com.lyf.scm.core.remote.stock.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.engine.jdbc.batch.spi.Batch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类OrderLine的实现描述：单据列表
 *
 * @author sunyj 2019/4/11 20:55
 */
@Data
public class OrderLine implements Serializable {

	@ApiModelProperty(value = "商品编码")
	@NotBlank(message = "商品编码不能为空")
	private String itemCode;

	@ApiModelProperty(value = "行号")
	@NotBlank(message = "行号不能为空")
	private String orderLineNo;

	@ApiModelProperty(value = "Po行号")
	private String poLineNo;

	@ApiModelProperty(value = "交货单行号")
	private String deliveryLineNo;

	@ApiModelProperty(value = "库存类型(ZP=正品 CC=残次 JS=机损 XS=箱损 ZT在途库存 默认为查所有类型的库存)")
	private String inventoryType;

	@ApiModelProperty(value = "应发商品数量")
	private BigDecimal planQty;

	@ApiModelProperty(value = "实发商品数量")
	@NotNull(message = "实发商品数量不能为空")
	private BigDecimal actualQty;

	@ApiModelProperty(value = "批次列表")
	private List<Batch> batchs;

	@ApiModelProperty(value = "扩展属性")
	private Map<String, Object> extendProps;

	@ApiModelProperty("单位")
	@NotBlank(message = "单位编码不能为空")
	private String unit;

}