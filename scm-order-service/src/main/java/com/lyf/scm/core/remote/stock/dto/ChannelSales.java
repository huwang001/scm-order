package com.lyf.scm.core.remote.stock.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChannelSales {

	/**
	 * 唯一主键
	 */
	@ApiModelProperty(value = "唯一主键")
	private Long id;
	/**
	 * 渠道编号
	 */
	@ApiModelProperty(value = "渠道编号")
	private String channelCode;
	/**
	 * 渠道名字
	 */
	@ApiModelProperty(value = "渠道名字")
	private String channelName;
	/**
	 * 策略组id
	 */
	@ApiModelProperty(value = "策略组id")
	private Long virtualWarehouseGroupId;

	@ApiModelProperty(value = "虚拟仓库组编码")
	private String virtualWarehouseGroupCode;

	/**
	 * 暂时不用，显示比率（百分比），大于0区间可选数字
	 */
	@ApiModelProperty(value = "显示比率")
	private Integer showRate;
	/**
	 * 商家id
	 */
	@ApiModelProperty(value = "商家id")
	private Long merchantId;
	/**
	 * 接单类型，0-库存不足拒绝，1-不可拒绝
	 */
	@ApiModelProperty(value = "接单类型，0-库存不足拒绝，1-不可拒绝")
	private Integer receiptType;

	@ApiModelProperty(value = "创建人")
	private Long creator;

	@ApiModelProperty("更新人")
	private Long modifier;
}
