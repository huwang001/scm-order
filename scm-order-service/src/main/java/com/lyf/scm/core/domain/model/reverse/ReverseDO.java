package com.lyf.scm.core.domain.model.reverse;

import java.io.Serializable;
import java.util.Date;

import com.lyf.scm.core.api.dto.BaseDTO;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

/**
 * @Description: 冲销单数据结构原始对象 <br>
 *
 * @Author wwh 2020/7/16
 */
@Data
public class ReverseDO extends BaseDO implements Serializable {
	
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 单据类型 1出库单冲销 2入库单冲销
     */
    private Integer recordType;

    /**
     * 单据状态 1已新建 2已取消 3已确认 11已出库 12已入库 13已过账
     */
    private Integer recordStatus;

    /**
     * 原始出/入库单据编号
     */
    private String originRecordCode;

    /**
     * 外部单据编号（出入库单对应的前置单号）
     */
    private String outRecordCode;
    
    /**
     * 收货单据编号（入库单冲销时必填）
     */
    private String receiptRecordCode;

    /**
     * SAP凭证号
     */
    private String voucherCode;

    /**
     * 实仓编码
     */
    private String realWarehouseCode;

    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 冲销日期
     */
    private Date reverseDate;

    /**
     * 备注
     */
    private String remark;

}