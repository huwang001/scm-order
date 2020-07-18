package com.lyf.scm.core.domain.model.stockFront;

import com.lyf.scm.core.domain.model.common.BaseDO;

import lombok.Data;

/**
 * 前置单与仓库库单关系
 */
@Data
public class FrontWarehouseRecordRelationDO extends BaseDO {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 前置单据编号
     */
    private Long frontRecordId;

    /**
     * 仓库的单据编号
     */
    private Long warehouseRecordId;

    /**
     * 前置单类型
     */
    private Integer frontRecordType;

    /**
     * 前置单号
     */
    private String frontRecordCode;
    
    /**
     * 关联出入库单号
     */
    private String dependRecordCode;
}