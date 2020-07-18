package com.lyf.scm.admin.dto.order;

import lombok.Data;

/**
 *
 */
@Data
public class OrderStatusDTO {

    /**
     * 实仓Code
     */
    private Integer orderStatusCode;

    /**
     * 调入虚仓编码
     */
    private String orderStatusName;
}

