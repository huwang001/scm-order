package com.lyf.scm.admin.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description 收货单明细DTO
 * @Author wuyuanhang
 * @Date 2020/7/18
 */
@Data
public class ReceiverRecordDetailDTO implements Serializable {

    private static final long serialVersionUID = 8436767449346149403L;

    @ApiModelProperty("商品编号")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "实际商品数量")
    private BigDecimal actualQty;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("采购单PO行号")
    private String lineNo;

    @ApiModelProperty("交货单行号")
    private String deliveryLineNo;
}
