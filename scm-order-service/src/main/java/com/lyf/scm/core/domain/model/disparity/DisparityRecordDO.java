package com.lyf.scm.core.domain.model.disparity;

import java.io.Serializable;
import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

/**
 * 差异单DO
 */
@Data
public class DisparityRecordDO extends BaseDO implements Serializable {
	
	private static final long serialVersionUID = 141129436442574162L;
	
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
	 * 入向工厂编号
	 */
	private String inFactoryCode;

	/**
	 * 入向仓库外部编号
	 */
	private String inRealWarehouseOutCode;


	/**
	 * 出向实体仓库id
	 */
	private Long outRealWarehouseId;


	/**
	 * 出向工厂编号
	 */
	private String outFactoryCode;

	/**
	 * 出向仓库外部编号
	 */
	private String outRealWarehouseOutCode;
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
	 * sap采购单号
	 */
	private String sapPoNo;

	/**
	 * sap交货单号
	 */
	private String sapDeliveryCode;



	
}
