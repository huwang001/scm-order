package com.lyf.scm.core.domain.model.disparity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lyf.scm.core.domain.model.common.BaseDO;

import lombok.Data;

/**
 * 差异单明细DO
 */
@Data
public class DisparityDetailDO extends BaseDO implements Serializable {
	
	private static final long serialVersionUID = -1426974651133895519L;
	
	/**
	 * 唯一主键
	 */
	private Long id;
	/**
	 * 单据编码
	 */
	private String recordCode;
	/**
	 * 差异单编码
	 */
	private Long disparityId;
	/**
	 * 商品sku编码
	 */
	private Long skuId;
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
	 * 入库仓库数据源
	 */
	private Long handlerInRealWarehouseId;

	/**
	 * 工厂编号
	 */
	private String handlerInFactoryCode;

	/**
	 * 仓库外部编号
	 */
	private String handlerInRealWarehouseOutCode;



	/**
	 * 备注：承运商信息，物流责任用
	 */
	private String remark;

	/**
	 * 成本中心，物流责任用
	 */
	private String costCenter;

	private String lineNo;

	/**
	 * 交货单行号
	 * */
	private String deliveryLineNo;

	private String sapPoNo;

	/**
	 * 前置单据关联主键
	 */
	private Long frontRecordId;
	/**
	 * 前置单据编码
	 */
	private String frontRecordCode;

	private Integer recordStatus;

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
































}
