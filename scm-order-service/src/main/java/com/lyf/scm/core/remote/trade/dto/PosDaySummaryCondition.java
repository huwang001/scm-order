package com.lyf.scm.core.remote.trade.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 门店日结查询条件
 */
@Data
@EqualsAndHashCode
public class PosDaySummaryCondition {

    /**
     * 门店编号
     */
    private String shopCode;

    /**
     * 日结日期
     */
    private Long summaryDate;

}
