package com.lyf.scm.core.api.dto.pack;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 需求调拨明细传输对象
 * <p>
 * @Author: wwh 2020/7/7
 */
@Data
public class DemandAllotDetailDTO {
	
	 @ApiModelProperty(value = "成品商品编码")
	 @NotBlank(message = "成品商品编码不能为空")
	 private String parentSkuCode; 
	 
	 @ApiModelProperty(value = "商品编码")
	 @NotBlank(message = "商品编码不能为空")
	 private String skuCode;
	
	 @ApiModelProperty(value = "调拨数量")
	 @NotNull(message = "调拨数量不能为空")
	 private BigDecimal allotQty;
	 
	 @ApiModelProperty(value = "单位", hidden = true)
	 private String unit;
	 
	 @ApiModelProperty(value = "单位编码", hidden = true)
	 private String unitCode;
	 
	 @ApiModelProperty(value = "订单中心行号（业务单据明细关联主键）", hidden = true)
	 private String orderLineNo;
	 
}