package com.lyf.scm.core.domain.entity.stockFront;

import java.util.Date;
import java.util.List;

import com.lyf.scm.core.domain.model.stockFront.WhAllocationDO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

import lombok.Data;

/**
 * 类WhAllocationE的实现描述：仓库调拨
 *
 * @author sunyj 2019/5/13 11:58
 */
@Data
public class WhAllocationE extends WhAllocationDO {

	/**
	 * 调拨单明细
	 */
	private List<WhAllocationDetailE> frontRecordDetails;

	/**
	 * 入向仓库
	 */
	private RealWarehouse inWarehouse;

	/**
	 * 出向仓库
	 */
	private RealWarehouse outWarehouse;

	/**
	 * 商家id
	 */
	private Long merchantId;

	/**
	 * 外部单据时间
	 */
	private Date outCreateTime;

	/**
	 * 开始日期
	 */
	private Date startDate;

	/**
	 * 结束日期
	 */
	private Date endDate;

}