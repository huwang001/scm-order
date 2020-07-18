package com.lyf.scm.core.remote.sap.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 类SyncSapPoDTO的实现描述：下发PO至sap
 *
 * @author sunyj 2019/6/25 10:26
 */
@Data
public class SyncSapPoDTO {

	/**
	 * 公司代码
	 */
	private String companyCode;

	/**
	 * 中台订单号
	 */
	private String billNo;

	/**
	 * 中台行项目
	 */
	private String billNum;

	/**
	 * 业务类型 01跨公司采购 02跨公司采购退货 03同公司采购 04同公司采购退货 05加盟采购 06加盟采购退货 07委外采购 08委外采购退货
	 * 09供应商直送
	 */
	private String type;

	/**
	 * 调拨日期
	 */
	private String reqDate;

	/**
	 * 物料号
	 */
	private String materialCode;

	/**
	 * 发货工厂
	 */
	private String sendPlant;

	/**
	 * 发货库位
	 */
	private String sendStorage;

	/**
	 * 到货门店
	 */
	private String recvPlant;

	/**
	 * 到货库位
	 */
	private String recvStorage;

	/**
	 * 数量
	 */
	private BigDecimal quantity;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 基准数量
	 */
	private BigDecimal baseQuantity;

	/**
	 * 基准单位
	 */
	private String baseUnit;

	/**
	 * 备注1
	 */
	private String remark1;

	/**
	 * 备注2
	 */
	private String remark2;

	/**
	 * 退货原因
	 */
	private String reason;

	/**
	 * 供应商号
	 */
	private String supplierCode;

	/**
	 * 跨公司采购
	 */
	public static final String DIFF_COMPANY_PURCHASE = "01";

	/**
	 * 跨公司采购退货
	 */
	public static final String DIFF_COMPANY_RETURN = "02";

	/**
	 * 同公司采购
	 */
	public static final String SAME_COMPANY_PURCHASE = "03";

	/**
	 * 同公司采购退货
	 */
	public static final String SAME_COMPANY_RETURN = "04";

}