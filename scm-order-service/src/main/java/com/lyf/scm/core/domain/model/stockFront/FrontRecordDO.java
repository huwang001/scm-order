package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.RealWarehouseE;
import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 类FrontRecord的实现描述: 通用前置单
 *
 * @author sunyj 2019/4/17 21:46
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class FrontRecordDO extends BaseDO implements Serializable {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 单据编号
     */
    private String recordCode;
    /**
     * 单据类型
     */
    private Integer recordType;
    /**
     * 单据状态
     */
    private Integer recordStatus;
    /**
     * 外部单据编号
     */
    private String outRecordCode;
    /**
     * 外部单据时间
     */
    private Date outCreateTime;
    /**
     * 审核原因
     */
    private String recordStatusReason;

    /**
     * 入向仓库
     */
    private RealWarehouseE inWarehouse;

    /**
     * 出向仓库
     */
    private RealWarehouseE outWarehouse;

    /**
     * 供应商编码代码
     */
    private String supplierCode;
}
