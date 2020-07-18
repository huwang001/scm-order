package com.lyf.scm.core.api.dto.reverse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 收货单明细DTO
 * @Author wuyuanhang
 * @Date 2020/7/18
 */
@Data
public class ReceiverRecordDetailDTO implements Serializable {

    private static final long serialVersionUID = -3750553217210466123L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("商品ID")
    private Long skuId;

    @ApiModelProperty("商品编码")
    private String skuCode;

    @ApiModelProperty("实仓ID")
    private Long realWarehouseId;

    @ApiModelProperty("")
    private String recordCode;

    @ApiModelProperty("")
    private String batchCode;

    @ApiModelProperty("业务类型")
    private Integer businessType;

    @ApiModelProperty("")
    private Date productDate;

    @ApiModelProperty("")
    private Date expireDate;

    @ApiModelProperty("")
    private String produceCode;

    @ApiModelProperty("")
    private String inventoryType;

    @ApiModelProperty(value = "实际商品数量")
    private BigDecimal actualQty;

    @ApiModelProperty("收货单编码")
    private String wmsRecordCode;

    @ApiModelProperty("")
    private String qualityStatus;

    @ApiModelProperty("")
    private String qualityCode;

    @ApiModelProperty("")
    private BigDecimal skuQty;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("")
    private String ztLineNo;

    @ApiModelProperty("采购单PO行号")
    private String lineNo;

    @ApiModelProperty("交货单行号")
    private String deliveryLineNo;
}
