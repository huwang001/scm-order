package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarehouseRecordDetailDTO implements Serializable {

	@ApiModelProperty(value = "唯一主键")
	private Long id;

	@JsonIgnore
	@ApiModelProperty(value = "所属单据编码")
	private String recordCode;

	@ApiModelProperty(value = "商品批次明细")
	private List<BatchStockDTO> batchStocks;

	@JsonIgnore
	@ApiModelProperty(value = "用户code")
	private String userCode;

	@ApiModelProperty(value = "渠道id")
	private String channelId;

	@ApiModelProperty(value = "店铺id")
	private Long shopId;

	@ApiModelProperty(value = "商品sku主键")
	private Long skuId;

	@ApiModelProperty(value = "sku编码")
	private String skuCode;

	@ApiModelProperty(value = "商品名称")
	private String skuName;

	@ApiModelProperty(value = "计划出/入库数量")
	private BigDecimal planQty;

	@ApiModelProperty(value = "实际出/入库数量")
	private BigDecimal actualQty;

	@ApiModelProperty(value = "计件单位名称")
	private String unit;

	@ApiModelProperty(value = "计件单位")
	private String unitCode;

	@ApiModelProperty(value = "商品总金额")
	private BigDecimal skuItemAmount;

	@ApiModelProperty(value = "商品销售单价")
	private BigDecimal skuPriceFinal;

	@ApiModelProperty(value = "商品编码")
	private String code;

	@ApiModelProperty(value = "第三方商品编码")
	private String thirdMerchantProductCode;

	@ApiModelProperty(value = "虚拟仓库ID")
	private Long virtualWarehouseId;

	@ApiModelProperty(value = "实仓ID")
	private Long realWarehouseId;

	private String lineNo;

	@ApiModelProperty(value = "超收比例 目前大仓采购使用")
	private Integer overReceiveRatio;

	@ApiModelProperty(value = "交货不足比例 1-100区间可选数字,目前大仓采购在使用")
	private Integer insufficientRatio;

	private Integer lineStatus;

	@ApiModelProperty(value = "实际单位的数量")
	private BigDecimal skuQty;

	private Date deliveryData;

	@ApiModelProperty(value = "商品条码")
	private String barCode;

	@ApiModelProperty(value = "中台单行号")
	private String ztLineNo;

	@ApiModelProperty(value = "交货单行号")
	private String deliveryLineNo;

	@ApiModelProperty(value = "sap采购单号")
	private String sapPoNo;

}