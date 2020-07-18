package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.remote.item.dto.SkuQtyUnitBase;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 仓库单据详情
 */
@Data
public class WarehouseRecordDetailDO extends SkuQtyUnitBase implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 所属单据编码
	 */
	private String recordCode;

	/**
	 * 所属单据id
	 */
	private Long warehouseRecordId;

	/**
	 * 用户code
	 */
	private String userCode;

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
	 * 商品数量
	 */
	private BigDecimal planQty;

	/**
	 * 基本计件单位名称
	 */
	private String unit;

	/**
	 * 基本计件单位
	 */
	private String unitCode;

	/**
	 * 虚拟仓库ID
	 */
	private Long virtualWarehouseId;

	/**
	 * 实仓ID
	 */
	private Long realWarehouseId;

	/**
	 * 实际收货数量 包括待质检的
	 */
	private BigDecimal actualQty;

	private String channelCode;

	private BigDecimal skuItemAmount;

	private BigDecimal skuPriceFinal;

	private String thirdMerchantProductCode;

	/**
	 * sap采购单号
	 */
	private String sapPoNo;

	/**
	 * 采购单行号
	 */
	private String lineNo;

	/**
	 * 交货单行号
	 */
	private String deliveryLineNo;

	/**
	 * 期望交货时间
	 */
	private Date deliveryData;

	/**
	 * 是否免费sku行 0:不免费 1:免费
	 */
	private Integer isFree;

}