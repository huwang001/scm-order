/**    
 * @Title: DisparityRecordDTO.java  
 * @Package: com.lyf.scm.core.api.dto.order  
 * @author: Lin.Xu  
 * @date: 2020-7-10 20:30:04
 * @version: v1.0    
 */  
    
package com.lyf.scm.core.api.dto.disparity;

import lombok.Data;

/**
 * @ClassName: DisparityRecordDTO  
 * @Description: 差异订单DTO  
 * @author: Lin.Xu  
 * @date: 2020-7-10 20:30:04
 * @version: v1.0
 */
@Data
public class DisparityRecordDTO {
	
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
	 * 出向实体仓库id
	 */
	private Long outRealWarehouseId;
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
