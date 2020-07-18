package com.lyf.scm.core.api.dto.stockFront;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @Description: RecordPackage
 *               <p>
 * @Author: chuwenchao 2019/9/5
 */
@Data
public class RecordPackageDTO implements Serializable {

	/**
	 * 唯一主键
	 */
	private Long id;

	/**
	 * 出入库单据编号
	 */
	private String recordCode;

	/**
	 * 仓库系统编码 1大福 2旺店通 3SAP-WM 4欧电云 5邮政 6玄天
	 */
	private Integer wmsCode;

	/**
	 * 物流公司编码
	 */
	private String logisticsCode;

	/**
	 * 物流公司名称
	 */
	private String logisticsName;

	/**
	 * 运单号
	 */
	private String expressCode;

	/**
	 * 包裹编号
	 */
	private String packageCode;

	/**
	 * 包裹重量
	 */
	private BigDecimal weight;

	/**
	 * 同步tms状态 0待同步 1同步成功
	 */
	private Integer syncTmsStatus;

	/**
	 * 包裹明细
	 */
	private List<RecordPackageDetailDTO> details;
    
    /**
     * 创建时间
     */
    private Date createTime;

}