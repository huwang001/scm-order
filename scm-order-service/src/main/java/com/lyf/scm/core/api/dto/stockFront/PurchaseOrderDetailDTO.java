package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购入库通知单明细
 */
@Data
@EqualsAndHashCode
public class PurchaseOrderDetailDTO {
	@ApiModelProperty(value = "行号")
	private String lineNo;

	@ApiModelProperty(value = "数量")
	@NotNull(message = "商品数量不能为空")
	@Digits(integer = 9, fraction = 3,message="超过范围,小数3位有效位，整数9位有效位")
	private BigDecimal skuQty;

	@ApiModelProperty(value = "skuId")
	private Long skuId;

	@ApiModelProperty(value = "sku编号")
	@NotEmpty(message = "sku编码为空")
	private String skuCode;

	@ApiModelProperty(value = "单位")
	@NotEmpty(message = "sku单位不能为空")
	private String unit;

	@ApiModelProperty(value = "单位编码")
	@NotEmpty(message="单位code不能为空")
	private String unitCode;

	@ApiModelProperty(value = "超收比例")
	private Integer overReceiveRatio = 0;


	@ApiModelProperty(value = "交货不足比例")
	private Integer insufficientRatio = 0;

	@ApiModelProperty(value = "行项目状态:1 待收货 2已收货 3已删除")
	@NotNull(message = "行项目状态:1 待收货 2已收货 3已删除")
	@Range(min=1, max=3,message = "行项目状态:1 待收货 2已收货 3已删除")
	private Integer status;

	@ApiModelProperty(value = "交货日期")
	private Date deliveryData;

	@ApiModelProperty(value = "是否免费行: 0:非免费行 1:免费行")
	private Integer isFree;
}
