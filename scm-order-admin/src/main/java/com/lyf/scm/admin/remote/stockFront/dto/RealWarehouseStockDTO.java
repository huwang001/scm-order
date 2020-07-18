/**
 * Filename RealWarehouseStockDo.java
 * Company 上海来伊份科技有限公司。
 * @author xly
 * @version 
 */
package com.lyf.scm.admin.remote.stockFront.dto;

import java.math.BigDecimal;
import java.util.List;

import com.lyf.scm.admin.remote.dto.BaseDTO;

import lombok.Data;

/**
 * 类BaseDo的实现描述：真实仓库存Do表
 * 
 * @author xly
 * @since 2019年4月21日 上午10:06:24
 */
@Data
public class RealWarehouseStockDTO extends BaseDTO {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 实仓ID
	 */
	private Long realWarehouseId;

	/**
	 * 商品sku编码
	 */
	private Long skuId;

	/**
	 * 真实库存
	 */
	private BigDecimal realQty;

	/**
	 * 锁定库存
	 */
	private BigDecimal lockQty;

	/**
	 * 在途库存
	 */
	private BigDecimal onroadQty;

	/**
	 * 质检库存
	 */
	private BigDecimal qualityQty;

	/**
	 * 不合格库存（一般是质检不合格库存）
	 */
	private Long unqualifiedQty;

	/**
	 * 商家id
	 */
	private Long merchantId;

	/**
	 * sku编号
	 */
	private String skuCode;

	/**
	 * sku名称
	 */
	private String skuName;

	/**
	 * 类目名称
	 */
	private String categoryName;

	/**
	 * sku的单位信息
	 */
	private List<WhSkuUnitDTO> skuUnitList;

	/**
	 * 基本数量
	 */
	private Long baseNum;

	/**
	 * 基本数量
	 */
	private String baseUnit;

	/**
	 * 批量sku编号字符串
	 */
	private String skuCodes;

	/**
	 * 批量sku编号集合
	 */
	private List<String> skuCodeList;

	/**
	 * 是否质量问题调拨 1是 0不是
	 */
	private Integer isQualityAllotcate;

	/**
	 * 实仓外部编码
	 */
	private String realWarehouseOutCode;

	/**
	 * 工厂编码
	 */
	private String factoryCode;

}