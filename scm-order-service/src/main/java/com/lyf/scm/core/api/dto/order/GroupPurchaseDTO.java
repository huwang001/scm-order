package com.lyf.scm.core.api.dto.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 同步发货信息给销售中心DTO
 */
@Data
public class GroupPurchaseDTO implements Serializable {

    /**
     * 出库单主键
     */
    private Long warehouseRecordId;
    /**
     * 预约单号
     */
    private String outRecordCode;

}
