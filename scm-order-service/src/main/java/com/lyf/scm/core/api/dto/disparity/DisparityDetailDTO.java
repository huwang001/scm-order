package com.lyf.scm.core.api.dto.disparity;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @ClassName: DisparityDetailDTO  
 * @Description: 差异订单明细DTO  
 * @author: Lin.Xu  
 * @date: 2020-7-10 20:27:53
 * @version: v1.0
 */
@Data
public class DisparityDetailDTO {

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
	 * 备注：承运商信息，物流责任用
	 */
	private String remark;

	/**
	 * 成本中心，物流责任用
	 */
	private String costCenter;

	/**
	 * 行号
	 */
	private String lineNo;

	/**
	 * 交货单行号
	 * */
	private String deliveryLineNo;

	/**
	 * sap单号
	 */
	private String sapPoNo;

	/**
	 * 前置单据关联主键
	 */
	private Long frontRecordId;
	/**
	 * 前置单据编码
	 */
	private String frontRecordCode;
	/**
	 * 单据状态
	 */
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
