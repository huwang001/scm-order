package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 接收库存中心退货入库通知详情传输对象
 * <p>
 * @Author: wwh 2020/4/10
 */
@Data
public class ReturnDetailNoticeDTO implements Serializable {
	
	@ApiModelProperty(value = "商品编码")
	@NotBlank(message="商品编码不能为空")
    private String skuCode;

	@ApiModelProperty(value = "实际收货数量")
	@NotNull(message="实际收货数量不能为空")
    private BigDecimal entryQty;

	@ApiModelProperty(value = "单位名称")
    private String unit;

	@ApiModelProperty(value = "单位编码")
	@NotBlank(message="单位编码不能为空")
    private String unitCode;

}