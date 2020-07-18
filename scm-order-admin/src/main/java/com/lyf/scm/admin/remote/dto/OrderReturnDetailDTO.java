package com.lyf.scm.admin.remote.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 退货单详情传输对象
 * <p>
 * @Author: wwh 2020/4/8
 */
@Data
public class OrderReturnDetailDTO extends BaseDTO implements Serializable {
	
	@ApiModelProperty(value = "唯一主键")
    private Long id;

	@ApiModelProperty(value = "售后单号")
    private String afterSaleCode;

	@ApiModelProperty(value = "商品编码")
    private String skuCode;

	@ApiModelProperty(value = "退货数量")
    private BigDecimal returnQty;

	@ApiModelProperty(value = "实际发货数量")
    private BigDecimal deliveryQty;

	@ApiModelProperty(value = "实际收货数量")
    private BigDecimal entryQty;

	@ApiModelProperty(value = "单位名称")
    private String unit;

	@ApiModelProperty(value = "单位编码")
    private String unitCode;
	
	@ApiModelProperty(value = "商品名称")
    private String skuName;

}