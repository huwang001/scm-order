package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdjustForetasteDO extends BaseDO implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 仓库id
     */
    private Long realWarehouseId;

    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 外部仓库编码
     */
    private String realWarehouseCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 单据类型：24-门店试吃单
     */
    private Integer recordType;

    /**
     * 单据状态：0-新建，1-审核 2-取消
     */
    private Integer recordStatus;

    /**
     * 外部系统单据编号
     */
    private String outRecordCode;

    /**
     * 外部系统数据创建时间
     */
    private Date outCreateTime;

    /**
     * sap过账单号
     */
    private String sapRecordCode;

    /**
     * 业务原因
     */
    private Integer reason;
}