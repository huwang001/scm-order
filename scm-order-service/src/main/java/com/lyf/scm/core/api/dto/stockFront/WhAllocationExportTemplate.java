package com.lyf.scm.core.api.dto.stockFront;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 类WhAllocationExportTemplate的实现描述：调拨导出模板
 *
 * @author sunyj 2019/11/4 14:45
 */
@Data
public class WhAllocationExportTemplate {

	/**
	 * 调拨单号
	 */
	private String recordCode;

	/**
	 * 调拨单状态
	 */
	private String recordTypeStr;

	/**
	 * SAP交货单号
	 */
	private String sapOrderCode;

	/**
	 * TMS派车单号
	 */
	private String tmsRecordCode;

	/**
	 * SAP同公司PO单号
	 */
	private String sapPoNo;

	/**
	 * 调出工厂编号
	 */
	private String outFactoryCode;

	/**
	 * 调出仓库编号
	 */
	private String outWarehouseCode;

	/**
	 * 调入工厂编号
	 */
	private String inFactoryCode;

	/**
	 * 调入仓库编号
	 */
	private String inWarehouseCode;

	/**
	 * 调拨类型
	 */
	private String businessTypeStr;

	/**
	 * 调拨日期
	 */
	private Date allotTime;

	/**
	 * 创建日期
	 */
	private Date createTime;

	/**
	 * 商品编号
	 */
	private String skuCode;

	/**
	 * 商品名称
	 */
	private String skuName;

	/**
	 * 初始调拨数量
	 */
	private BigDecimal orginQty;

	/**
	 * 差异调拨数量
	 */
	private BigDecimal diffQty;

	/**
	 * 调拨单位
	 */
	private String unit;

	/**
	 * 调拨出库数量
	 */
	private BigDecimal outQty;

	/**
	 * 调拨出库基本数量
	 */
	private BigDecimal outBasicQty;

	/**
	 * 基本单位
	 */
	private String basicUnit;

	/**
	 * 预下市信息
	 */
	private String underCarriageStr;

}
