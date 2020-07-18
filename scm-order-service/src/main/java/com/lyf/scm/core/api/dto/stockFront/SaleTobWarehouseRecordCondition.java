package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class SaleTobWarehouseRecordCondition extends BaseDTO implements Serializable {

	@ApiModelProperty(value = "出库单编号")
	private String recordCode;

	@ApiModelProperty(value = "前置单号")
	private String frontRecordCode;

	@ApiModelProperty(value = "入门店code")
	private String inShopCode;

	@ApiModelProperty(value = "商品code")
	private String skuCode;

	@ApiModelProperty(value = "渠道类型")
	private String channelCodes;

	@ApiModelProperty(value = "单据类型")
	private Integer recordType;

	@ApiModelProperty(value = "出仓id")
	private Long outRealWarehouseId;

	@ApiModelProperty(value = "工厂编码")
	private String outFactoryCode;

	@ApiModelProperty(value = "出仓Code")
	private String outRealWarehouseCode;

	@ApiModelProperty(value = "出库单创建开始时间")
	private Date startTime;

	@ApiModelProperty(value = "出库单创建结束时间")
	private Date endTime;

	@ApiModelProperty(value = "单据状态")
	private Integer recordStatus;

	@ApiModelProperty(value = "发货单号集合")
	private List<String> recordCodes;

	@ApiModelProperty(value = "发货单id集合")
	private List<Long> ids;

	@ApiModelProperty(value = "渠道编码集合")
	private List<String> channelCodeList;

	@ApiModelProperty(value = "sap单据编号")
	private String sapPoNo;

	@ApiModelProperty(value = "外部系统单据编号")
	private String outRecordCode;

	@ApiModelProperty(value = "tms派车单号")
	private String tmsRecordCode;

	@ApiModelProperty(value = "sap单号集合")
	private List<String> sapOrderCodes;

	@ApiModelProperty(value = "入门店code")
	private String shopCode;

}