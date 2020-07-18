package com.lyf.scm.core.api.dto.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 库存回调明细
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
@Data
public class StockNotifyDetailDTO implements Serializable {

    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    @ApiModelProperty(value = "实际出/入库数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "计件单位名称")
    private String unit;

    @ApiModelProperty(value = "PO单行号")
    private String lineNo;

    @ApiModelProperty(value = "交货单行号")
    private String deliveryLineNo;
}
