package com.lyf.scm.admin.dto.disparity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 差异单DO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DisparityRecordDetail {
	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 差异单的主键id
	 */
	private Long disparityId;
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

	private String inFactoryCode;

	private String outFactoryCode;
	/**
	 * 入向实体仓库名称
	 */
	private String inRealWarehouseName;
	/**
	 * 入向实体仓库编号
	 */
	private String inRealWarehouseCode;
	/**
	 * 出向实体仓库id
	 */
	private Long outRealWarehouseId;
	/**
	 * 出向实体仓库编号
	 */
	private String outRealWarehouseCode;
	/**
	 * 出向实体仓库名称
	 */
	private String outRealWarehouseName;
	/**
	 * 单据状态
	 */
	private Integer recordStatus;
	/**
	 * 责任方
	 */
	private String responsible;
	/**
	 * 责任方类型
	 */
	private Integer responsibleType;
	/**
	 * 责任原因
	 */
	private Integer reasons;
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
	 * 创建时间
	 */
	private Date updateTime;
	/**
	 * 商品sku编码
	 */
	private Long skuId;
	/**
	 * 商品名称
	 */
	private String skuName;
	/**
	 * 商品sku编码
	 */
	private String skuCode;
	/**
	 * 差异数量
	 */
	private BigDecimal skuQty;
	/**
	 * 入库数量
	 */
	private BigDecimal inSkuQty;
	/**
	 * 出库数量
	 */
	private BigDecimal outSkuQty;
	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 单位code
	 */
	private String unitCode;

	private Long modifier;
	private Long creator;

	/**
	 * 员工号
	 */
	private String employeeNumber;
	/**
	 * 员工号
	 */
	private String createEmployeeNumber;

	/**
	 * sap采购单号
	 */
	private String sapPoNo;

	/**
	 * sap交货单号
	 */
	private String sapDeliveryCode;

	/**
	 * 入库仓库数据源
	 */
	private Long handlerInRealWarehouseId;

	/**
	 * 差异入库仓库名称
	 */
	private String handlerInRealWarehouseName;

	/**
	 * 差异入库仓库编号
	 */
	private String handlerInRealWarehouseCode;

	/**
	 * 差异入库工厂编号
	 */
	private String handlerInFactoryCode;

	/**
	 * 备注：承运商信息，物流责任用
	 */
	private String remark;

	/**
	 * 成本中心，物流责任用
	 */
	private String costCenter;

	/**
	 * 行号
	 * */
	private String lineNo;

	/**
	 * 交货单行号
	 * */
	private String deliveryLineNo;


	/**
	 * 单位比例关系
	 */
	private BigDecimal scale;

	/**
	 * 真实单位名称
	 */
	private String realUnit;
	/**
	 * 真实单位code
	 */
	private String realUnitCode;

	/**
	 * 差异数量
	 */
	private BigDecimal realSkuQty;
	/**
	 * 入库数量
	 */
	private BigDecimal realInSkuQty;
	/**
	 * 出库数量
	 */
	private BigDecimal realOutSkuQty;

	/**
	 * 门店编号
	 */
	private String shopCode;
}
