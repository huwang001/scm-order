package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 接收交易中心退货单详情传输对象
 * <p>
 * @Author: wwh 2020/4/8
 */
@Data
public class ReturnDetailDTO implements Serializable {
	
	@ApiModelProperty(value = "商品编码")
	@NotBlank(message="商品编码不能为空")
    private String skuCode;

	@ApiModelProperty(value = "退货数量")
	@NotNull(message="退货数量不能为空")
    private BigDecimal returnQty;

	@ApiModelProperty(value = "单位名称")
	@NotBlank(message="单位名称不能为空")
    private String unit;

	@ApiModelProperty(value = "单位编码")
	@NotBlank(message="单位编码不能为空")
    private String unitCode;

}