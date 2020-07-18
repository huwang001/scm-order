package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.WarehouseRecordDO;
import com.lyf.scm.core.remote.stock.dto.QueryRealWarehouseDTO;
import lombok.Data;

import java.util.List;

/**
 * 出入库单
 *
 * @author sunyj 2019/4/17 20:52
 */
@Data
public class WarehouseRecordE extends WarehouseRecordDO {

	/**
	 * 实仓仓库名称
	 */
	private String realWarehouseName;

	/**
	 * 出入库单明细
	 */
	private List<WarehouseRecordDetailE> warehouseRecordDetailList;

	/**
	 * 前置单编码
	 */
	private String frontRecordCode;

	/**
	 * 店铺编码
	 */
	private String shopCode;
	
	/**
	 * 虚仓编码
	 */
	private String vwCode;

	private List<QueryRealWarehouseDTO> realWhCodes;

}