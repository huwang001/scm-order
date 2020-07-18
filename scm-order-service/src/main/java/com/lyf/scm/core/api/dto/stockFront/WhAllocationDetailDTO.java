package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店调拨单详情
 *
 * @author sunyj 2019/4/21 17:06
 */
@Data
public class WhAllocationDetailDTO implements Serializable {

	@ApiModelProperty(value = "调拨数量")
	@NotNull(message = "调拨数量不能为空")
	@Range(min = 0, message = "调拨数量不能为负数")
	private BigDecimal allotQty;

	@ApiModelProperty(value = "sku编号")
	@NotNull(message = "sku编码为空")
	private Long skuId;

	@ApiModelProperty(value = "单位")
	@NotEmpty(message = "sku单位不能为空")
	private String unit;

	@ApiModelProperty(value = "单位")
	@NotEmpty(message = "单位code不能为空")
	private String unitCode;

	@ApiModelProperty(value = "批次备注")
	private String batchRemark;

	@ApiModelProperty(value = "实际调入数量")
	private BigDecimal inQty;

	@ApiModelProperty(value = "实际调出数量")
	private BigDecimal outQty;

	@ApiModelProperty(value = "商品名称")
	private String skuName;

	@ApiModelProperty(value = "虚仓比例")
	private List<VirtualWarehouse> vmSyncRate;

	@ApiModelProperty(value = "sku编号")
	private String skuCode;

	@ApiModelProperty(value = "类目名称")
	private String categoryName;

	@ApiModelProperty(value = "真实库存")
	private BigDecimal realQty;

	@ApiModelProperty(value = "锁定库存")
	private BigDecimal lockQty;

	@ApiModelProperty(value = "规格")
	private String spec;

	@ApiModelProperty(value = "基本数量")
	private Long baseNum;

	@ApiModelProperty(value = "基本单位")
	private String baseUnit;

	@ApiModelProperty(value = "sku的单位信息")
	private List<WhSkuUnitDTO> skuUnitList;

	@ApiModelProperty(value = "退货原因")
	private String reasonCode;

	@ApiModelProperty(value = "初始数量")
	private BigDecimal orginQty;

	@ApiModelProperty(value = "行号")
	private String lineNo;

}