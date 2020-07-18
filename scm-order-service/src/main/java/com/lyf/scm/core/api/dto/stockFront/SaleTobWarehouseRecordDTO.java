package com.lyf.scm.core.api.dto.stockFront;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SaleTobWarehouseRecordDTO {

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 出库单编号
	 */
	private String recordCode;

	/**
	 * sap单号
	 */
	private String sapOrderCode;

	/**
	 * 前置单号
	 */
	private String frontRecordCode;

	/**
	 * 前置单号集合字符串
	 */
	private String frontRecordCodes;

	/**
	 * 入仓id
	 */
	private Long inRealWarehouseId;

	/**
	 * 入仓编码
	 */
	private Long inRealWarehouseCode;

	/**
	 * 入向工厂编码
	 */
	private String inFactoryCode;

	/**
	 * 入仓name
	 */
	private String inRealWarehouseName;

	/**
	 * 出仓id
	 */
	private Long outRealWarehouseId;

	/**
	 * 出仓编码
	 */
	private String outRealWarehouseCode;

	/**
	 * 出向工厂编码
	 */
	private String outFactoryCode;

	/**
	 * 出仓name
	 */
	private String outRealWarehouseName;

	/**
	 * 门店编号
	 */
	private String shopCode;

	/**
	 * 商品code
	 */
	private String skuCode;

	/**
	 * 渠道类型
	 */
	private String channelCodes;

	/**
	 * 渠道编码
	 */
	private String channelCode;

	/**
	 * 供应工厂
	 */
	private String deliveryFactory;

	/**
	 * 渠道名称
	 */
	private String channelName;

	/**
	 * 单据状态
	 */
	private Integer recordStatus;

	/**
	 * 单据状态名称
	 */
	private String recordStatusName;

	/**
	 * 单据类型
	 */
	private Integer recordType;

	/**
	 * 单据类型名称
	 */
	private String recordTypeName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 商品详情
	 */
	private List<SaleTobWarehouseRecordDetailDTO> details;

	/**
	 * sap单据编号
	 */
	private String sapPoNo;

	/**
	 * 外部系统单据编号
	 */
	private String outRecordCode;

	/**
	 * tms派车单号
	 */
	private String tmsRecordCode;

	/**
	 * WMS推送状态
	 */
	private Integer syncWmsStatus;

	/**
	 * 修改人
	 */
	private Long modifier;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 是否有可能整单拒收处理
	 */
	private boolean disparityHandle;

	/**
	 * 前置单号集合
	 */
	private List<String> frontRecordList;

}