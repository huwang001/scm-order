package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lyf.scm.core.domain.model.common.BaseDO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarehouseRecordDTO extends BaseDO implements Serializable {

	@ApiModelProperty(value = "唯一主键")
	private Long id;

	@ApiModelProperty(value = "单据编码")
	private String recordCode;

	@ApiModelProperty(value = "业务类型")
	private Integer businessType;

	@ApiModelProperty(value = "do单状态 0未同步 1已同步 10拣货 11打包 12装车 13发运 21接单 22配送 23完成")
	private Integer recordStatus;

	@ApiModelProperty(value = "单据类型 1销售出库订单 2采购单")
	private Integer recordType;

	@ApiModelProperty(value = "用户code")
	private String userCode;

	@ApiModelProperty(value = "虚拟仓库ID")
	private Long virtualWarehouseId;

	@ApiModelProperty(value = "实仓ID")
	private Long realWarehouseId;

	@ApiModelProperty(value = "渠道id")
	private String channelCode;

	@ApiModelProperty(value = "店铺id")
	private Long shopId;

	@ApiModelProperty(value = "商家id")
	private Long merchantId;

	@ApiModelProperty(value = "订单备注(用户)")
	private String orderRemarkUser;

	@ApiModelProperty(value = "期望收货日期-开始")
	private Date expectReceiveDateStart;

	@ApiModelProperty(value = "期望收货日期-截止")
	private Date expectReceiveDateEnd;

	@ApiModelProperty(value = "收货人地址邮编")
	private String receiverPostcode;

	@ApiModelProperty(value = "收货人手机")
	private String receiverMobile;

	@ApiModelProperty(value = "收货人电话")
	private String receiverPhone;

	@ApiModelProperty(value = "收货人邮箱")
	private String receiverEmail;

	@ApiModelProperty(value = "收件人证件类型 1身份证 2军官证 3护照 4其他)")
	private Integer receiverIdType;

	@ApiModelProperty(value = "收货人证件号码")
	private String receiverIdNumber;

	@ApiModelProperty(value = "收货人国家")
	private String receiverCountry;

	@ApiModelProperty(value = "收货人省份")
	private String receiverProvince;

	@ApiModelProperty(value = "收货人城市")
	private String receiverCity;

	@ApiModelProperty(value = "收货人区/县城市")
	private String receiverCounty;

	@ApiModelProperty(value = "收货人四级区域")
	private String receiverArea;

	@ApiModelProperty(value = "收货人国家code")
	private String receiverCountryCode;

	@ApiModelProperty(value = "收货人省份code")
	private String receiverProvinceCode;

	@ApiModelProperty(value = "收货人城市code")
	private String receiverCityCode;

	@ApiModelProperty(value = "收货人区/县城市code")
	private String receiverCountyCode;

	@ApiModelProperty(value = "收货人四级区域code")
	private String receiverAreaCode;

	@ApiModelProperty(value = "收货人详细地址")
	private String receiverAddress;

	@ApiModelProperty(value = "收货人姓名")
	private String receiverName;

	@ApiModelProperty(value = "撤回原因")
	private String reasons;

	@ApiModelProperty(value = "异常原因")
	private String errorMessage;

	@ApiModelProperty(value = "撤回时间")
	private Date relinquishTime;

	@ApiModelProperty(value = "发货时间")
	private Date deliveryTime;

	@ApiModelProperty(value = "收货时间")
	private Date receiverTime;

	@ApiModelProperty(value = "单据同步wms状态")
	private Integer syncWmsStatus;

	private String realWarehouseCode;

	private Date createTime;

	@ApiModelProperty(value = "sku数量及明细")
	private List<WarehouseRecordDetailDTO> warehouseRecordDetails;

	@ApiModelProperty(value = "包裹信息")
	private List<RecordPackageDTO> doPackages;

	@ApiModelProperty(value = "批次信息")
	private List<RwBatchDTO> batches;

	@ApiModelProperty(value = "wms入库单编码")
	private String entryOrder;

	@ApiModelProperty(value = "工厂编码")
	private String factoryCode;

	@ApiModelProperty(value = "外部仓库地址编码")
	private String realWarehouseOutCode;

	@ApiModelProperty(value = "sap单号")
	private String sapOrderCode;

	@ApiModelProperty(value = "tms派车单号")
	private String tmsRecordCode;

	@ApiModelProperty(value = "wms入库单状态")
	private String wmsStatus;

	@ApiModelProperty(value = "周转箱List")
	private List<TurnoverBoxDTO> turnoverBoxDTOS;

	@ApiModelProperty(value = "订单完成时间")
	private String operateTime;

}