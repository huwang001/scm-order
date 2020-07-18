package com.lyf.scm.core.api.dto.stockFront;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/3
 */
@Data
public class CommonFrontRecordDetailDTO implements Serializable {

    @ApiModelProperty(value = "唯一主键")
    private Long id;

    @ApiModelProperty(value = "所属单据编码")
    private String recordCode;

    @ApiModelProperty(value = "单据id")
    private Long frontRecordId;

    @ApiModelProperty(value = "sap采购单号")
    private String sapPoNo;

    @ApiModelProperty(value = "行号")
    private String lineNo;

    @ApiModelProperty(value = "商品skuID")
    private Long skuId;

    @ApiModelProperty(value = "商品sku编码")
    private String skuCode;


    //============== 门店补货字段 ==============
    @ApiModelProperty(value = "数量")
    private BigDecimal skuQty;

    @ApiModelProperty(value = "已分配数量")
    private BigDecimal allotQty;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单位编码")
    private String unitCode;

    //============== 调拨字段 ==============
    @ApiModelProperty(value = "实际调入数量")
    private BigDecimal inQty;

    @ApiModelProperty(value = "实际调出数量")
    private BigDecimal outQty;

    @ApiModelProperty(value = "批次备注")
    private String batchRemark;

    @ApiModelProperty(value = "退货原因")
    private String reasonCode;

    @ApiModelProperty(value = "原调拨单PO行号")
    private String originLineNo;

    @ApiModelProperty(value = "初始数量")
    private BigDecimal orginQty;

    //============== 差异字段 ==============
    @ApiModelProperty(value = "处理工厂编号")
    private String handlerInFactoryCode;

    @ApiModelProperty(value = "处理仓库外部编号")
    private String handlerInRealWarehouseOutCode;

    @ApiModelProperty(value = "责任方")
    private String responsible;

    @ApiModelProperty(value = "责任方类型:1-门店责任 2-仓库责任 3-物流责任")
    private Integer responsibleType;

    @ApiModelProperty(value = "责任原因:1-实物在收货仓 2-实物在发货仓 3-实物丢失 4-实物质量问题 5-仓库漏发")
    private Integer reasons;

    @ApiModelProperty(value = "备注：承运商信息，物流责任用")
    private String remark;

    @ApiModelProperty(value = "成本中心，物流责任用")
    private String costCenter;

    @ApiModelProperty(value = "入库数量")
    private BigDecimal inSkuQty;

    @ApiModelProperty(value = "出库数量")
    private BigDecimal outSkuQty;

    //============== 门店退货 ==============
    @ApiModelProperty(value = "实退数量")
    private BigDecimal realRefundQty;

    @ApiModelProperty(value = "实收数量")
    private BigDecimal realEnterQty;

    @ApiModelProperty(value = "含加成率价格")
    private BigDecimal additionRatePrice;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

}
