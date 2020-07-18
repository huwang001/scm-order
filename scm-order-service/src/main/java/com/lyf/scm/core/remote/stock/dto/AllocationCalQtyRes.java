package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;
import java.util.List;

import com.lyf.scm.core.domain.entity.stockFront.RecordRealVirtualStockSyncRelationE;

import lombok.Data;

/**
 * @Doc:计算虚仓分配数的结果
 * @Author: lchy
 * @Date: 2020/3/25
 * @Version 1.0
 */
@Data
public class AllocationCalQtyRes {

	private String lineNo;

	private Long skuId;

	private String skuCode;

	/**
	 * 调整前的数量【计划数量】
	 */
	private BigDecimal planQty;

	/**
	 * 调整前的数量【基本数量】
	 */
	private BigDecimal planBasicQty;

	private String unitCode;

	/**
	 * 调整后的数量【前置单数量】
	 */
	private BigDecimal actualQty;

	/**
	 * 调整后的数量【基本单位数量】
	 */
	private BigDecimal actualBasicQty;

	private String actualUnitCode;
	private String actualUnitName;
	private BigDecimal actualScale;

	/**
	 * 发货单位取整计算时的换算比例
	 */
	private BigDecimal scale;

	/**
	 * 是否需要箱单位或发货单位取整
	 */
	private Boolean considerRound;

	private String basicUnitCode;

	private String basicUnitName;
	/**
	 * 对于总仓来说，需要考虑发货单位取整问题，就要改变调整需求数，
	 */
	private Boolean isChangedPlan = false;
	
    /**
     * 是否存在某一个虚仓库存不足的情况,只要有一个虚仓不足,就是true
     */
    private Boolean existInSufficient = false;
	
	private List<VwAllocationQty> vwAllocationQtyList;

	private List<RecordRealVirtualStockSyncRelationE> list;

}