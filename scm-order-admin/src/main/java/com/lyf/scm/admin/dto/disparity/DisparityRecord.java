package com.lyf.scm.admin.dto.disparity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 差异单DO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DisparityRecord implements Serializable {
	/**
	 * 唯一主键
	 */
	private Long id;
	/**
	 * 差异单编码
	 */
	private String recordCode;
	/**
	 * 前置单据关联主键
	 */
	private Long frontRecordId;
	/**
	 * 前置单据编码
	 */
	private String frontRecordCode;
	/**
	 * 出入库单据关联主键
	 */
	private Long inWarehouseRecordId;
	/**
	 * 出入库单据编码
	 */
	private String inWarehouseRecordCode;
	/**
	 * 出入库单据关联主键
	 */
	private Long outWarehouseRecordId;
	/**
	 * 出入库单据编码
	 */
	private String outWarehouseRecordCode;
	/**
	 * 单据类型
	 */
	private Integer recordType;
	/**
	 * 入向实体仓库id
	 */
	private Long inRealWarehouseId;
	/**
	 * 入向实体仓库名称
	 */
	private String inRealWarehouseName;
	/**
	 * 出向实体仓库id
	 */
	private Long outRealWarehouseId;
	/**
	 * 出向实体仓库名称
	 */
	private String outRealWarehouseName;
	/**
	 * 单据状态
	 */
	private Integer recordStatus;

	/**
	 * 运输方
	 */
	private String transporter;
	/**
	 * 商家id
	 */
	private Long merchantId;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * sap采购单号
	 */
	private String sapPoNo;

	/**
	 * sap交货单号
	 */
	private String sapDeliveryCode;

}
