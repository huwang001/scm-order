package com.lyf.scm.core.domain.entity.disparity;

import java.math.BigDecimal;
import java.util.Date;

import com.lyf.scm.core.domain.model.disparity.DisparityDetailDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DisparityRecordDetailE  
 * @Description: 差异订单明细E  
 * @author: Lin.Xu  
 * @date: 2020-7-10 20:15:28
 * @version: v1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class DisparityDetailE extends DisparityDetailDO  {

	private static final long serialVersionUID = -9124890885516469677L;


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

	private String shopCode;


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

	private String inRealWarehouseCode;
	/**
	 * 出向实体仓库id
	 */
	private Long outRealWarehouseId;
	/**
	 * 出向实体仓库名称
	 */
	private String outRealWarehouseName;

	private String outRealWarehouseCode;


	/**
	 * sap交货单号
	 */
	private String sapDeliveryCode;



	private String handlerInRealWarehouseCode;

	private String handlerInFactoryCode;

	private String handlerInRealWarehouseName;


	/**
	 * 运输方
	 */
	private String transporter;
	/**
	 * 商家id
	 */
	private Long merchantId;

	/**
	 * 商品名称
	 */
	private String skuName;







}
