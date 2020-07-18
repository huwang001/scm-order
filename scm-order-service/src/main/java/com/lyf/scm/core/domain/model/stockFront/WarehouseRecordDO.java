package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 库存仓库单据
 */
@Data
public class WarehouseRecordDO extends BaseDO implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 所属单据编码
	 */
	private String recordCode;

	/**
	 * 查询用前置单
	 */
	private FrontRecordE frontRecord;

	/**
	 * sap单号
	 */
	private String sapOrderCode;

	/**
	 * 业务类型
	 */
	private Integer businessType;

	/**
	 * 单据状态 0未同步 1已同步 10拣货 11打包 12装车 13发运 21接单 22配送 23完成
	 */
	private Integer recordStatus;

	/**
	 * 单据类型 1销售出库订单 2采购单
	 */
	private Integer recordType;

	/**
	 * 用户code
	 */
	private String userCode;

	/**
	 * 虚拟仓库ID
	 */
	private Long virtualWarehouseId;

	/**
	 * 虚拟仓库编码
	 */
	private String virtualWarehouseCode;

	/**
	 * 实仓ID
	 */
	private Long realWarehouseId;

	/**
	 * 实仓编码
	 */
	private String realWarehouseCode;

	/**
	 * 工厂编码
	 */
	private String factoryCode;

	/**
	 * 工厂名称
	 */
	private String factoryName;

	/**
	 * 渠道类型
	 */
	private Long channelType;

	/**
	 * 渠道id
	 */
	private String channelCode;

	/**
	 * 商家id
	 */

	private Long merchantId;

	/**
	 * 订单备注（用户）
	 */
	private String orderRemarkUser;

	/**
	 * 外部系统数据创建时间（下单时间）
	 */
	private Date outCreateTime;

	/**
	 * 支付时间
	 */
	private Date payTime;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 退货原因（退货流程用）
	 */
	private String reason;

	/**
	 * 期望收货日期-开始
	 */
	private Date expectReceiveDateStart;

	/**
	 * 期望收货日期-截止
	 */
	private Date expectReceiveDateEnd;

	/**
	 * 撤回原因
	 */
	private String reasons;

	/**
	 * 异常原因
	 */
	private String errorMessage;

	/**
	 * 撤回时间
	 */
	private Date relinquishTime;

	/**
	 * 发货时间
	 */
	private Date deliveryTime;

	/**
	 * 收货时间
	 */
	private Date receiverTime;

	/**
	 * 单据同步wms状态
	 */
	private Integer syncWmsStatus;

	/**
	 * 同步wms失败时间
	 */
	private Date syncWmsFailTime;

	/**
	 * 批次状态
	 */
	private Integer batchStatus;

	/**
	 * 开始日期
	 */
	private Date startDate;

	/**
	 * 结束日期
	 */
	private Date endDate;

	/**
	 * SAP派车状态 0无需派车 1待派车 2已派车
	 */
	private Integer syncDispatchStatus;

	/**
	 * 0无需过账 1待过账 2已过账（默认0）
	 */
	private Integer syncTransferStatus = 0;

	/**
	 * 0无需同步 1待同步 2已同步
	 */
	private Integer cmpStatus;

	/**
	 * 传输交易订单状态 0无需同步 1待同步 2已完成
	 */
	private Integer syncTradeStatus;

	/**
	 * tms派车单号
	 */
	private String tmsRecordCode;

	/**
	 * 是否是自营外卖
	 */
	private Integer selfTakeout = 0;

	private Integer syncFulfillmentStatus = 0;

	/**
	 * 仓库编码List
	 */
	private List<Long> warehouseIdList;

	/**
	 * 单据类型List
	 */
	private List<Integer> recordTypeList;

	/**
	 * 出库或入库完成时间
	 */
	private Date outOrInTime;

	/**
	 * 同步库存状态 0无需同步 1待同步 2已完成
	 */
	private Integer syncStockStatus;

	/**
	 * 同步派车系统状态 0无需同步 1待同步 2已完成
	 */
	private Integer syncTmsbStatus;

}