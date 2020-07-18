package com.lyf.scm.core.domain.model.stockFront;

import java.io.Serializable;
import java.util.Date;

import com.lyf.scm.core.domain.model.common.BaseDO;

import lombok.Data;

/**
 *
 * @author <sunyj>
 * @version 2019-05-12 19:49:49
 *
 */

@Data
public class WhAllocationDO extends BaseDO implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 单据编号
	 */
	private String recordCode;

	/**
	 * sap采购单号
	 */
	private String sapPoNo;

	/**
	 * 单据类型 22调拨单
	 */
	private Integer recordType;

	/**
	 * 0已新建 1已确认 2已取消 4已派车 10已出库 11已入库
	 */
	private Integer recordStatus;

	/**
	 * 1内部调拨 2RDC调拨 3退货调拨 4电商仓调拨 5质量问题调拨
	 */
	private Integer businessType;

	/**
	 * 1出入都是中台 2出中台入非中台 3出非中台入中台
	 */
	private Integer whType;

	/**
	 * 是否差异入库 0没有差异 1有差异
	 */
	private Integer isDiffIn;

	/**
	 * 入向仓库id
	 */
	private Long inWarehouseId;

	/**
	 * 入向实仓编码
	 */
	private String inRealWarehouseCode;

	/**
	 * 入向工厂编码
	 */
	private String inFactoryCode;

	/**
	 * 入向仓库联系人
	 */
	private String inWarehouseName;

	/**
	 * 入向联系电话
	 */
	private String inWarehouseMobile;

	/**
	 * 出向仓库id
	 */
	private Long outWarehouseId;

	/**
	 * 出向实仓编码
	 */
	private String outRealWarehouseCode;

	/**
	 * 出向工厂编码
	 */
	private String outFactoryCode;

	/**
	 * 出向仓库联系人
	 */
	private String outWarehouseName;

	/**
	 * 出向仓库电话
	 */
	private String outWarehouseMobile;

	/**
	 * 调拨日期
	 */
	private Date allotTime;

	/**
	 * 预计到货日期
	 */
	private Date expeAogTime;

	/**
	 * 审核人
	 */
	private Long auditor;

	/**
	 * 审核时间
	 */
	private Date auditTime;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 开始日期
	 */
	private Date startDate;

	/**
	 * 结束日期
	 */
	private Date endDate;

	/**
	 * SAP下发状态 0无需下发 1待下发 2已下发
	 */
	private Integer syncStatus;

	/**
	 * 是否退货调拨 1是 0不是
	 */
	private Integer isReturnAllotcate;

	/**
	 * 新增类型 1页面新增 2excel导入 3差异调拨
	 */
	private Integer addType;

	/**
	 * 是否存在差异 1存在差异 0不存在差异
	 */
	private Integer isDisparity;

	/**
	 * 原始单据Id
	 */
	private Long orginId;

	/**
	 * 原始单据号
	 */
	private String orginRecord;
	
	/**
	 * 调拨类型 1整箱调拨 2拆零调拨
	 */
	private Integer splitType;
	
	/**
	 * 是否质量问题调拨 1是 0不是
	 */
	private Integer isQualityAllotcate;

}