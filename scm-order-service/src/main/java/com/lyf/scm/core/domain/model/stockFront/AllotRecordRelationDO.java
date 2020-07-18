package com.lyf.scm.core.domain.model.stockFront;

import java.io.Serializable;

import com.lyf.scm.core.domain.model.common.BaseDO;

import lombok.Data;

/**
 * @Description: 调拨业务单据关系数据结构原始对象 <br>
 *
 * @Author wwh 2020/7/8
 */
@Data
public class AllotRecordRelationDO extends BaseDO implements Serializable {
	
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 调拨单号
     */
    private String allotCode;

    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 类型 1需求单
     */
    private Integer type;

    /**
     * 行号（调拨单明细关联主键）
     */
    private String lineNo;

    /**
     * 订单中心行号（业务单据明细关联主键）
     */
    private String orderLineNo;

}