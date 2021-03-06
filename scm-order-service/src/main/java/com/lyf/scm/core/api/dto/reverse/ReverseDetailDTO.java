package com.lyf.scm.core.api.dto.reverse;

import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 冲销单明细传输对象
 * <p>
 * @Author: wwh 2020/7/16
 */
@Data
public class ReverseDetailDTO implements Serializable {

    @ApiModelProperty(value = "主键编辑必传")
    private Long id;

    @ApiModelProperty(value = "单据编号")
    private String recordCode;

    @ApiModelProperty(value = "商品编码")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "单位名称")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    private String unitCode;

    @ApiModelProperty(value = "冲销数量")
    private BigDecimal reverseQty;

    @ApiModelProperty(value = "实际出/入库数量")
    private BigDecimal actualQty;

    @ApiModelProperty(value = "批次备注")
    private String batchRemark;

    @ApiModelProperty("采购单PO行号")
    private String lineNo;

    @ApiModelProperty("交货单行号")
    private String deliveryLineNo;

}