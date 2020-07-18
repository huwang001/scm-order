package com.lyf.scm.core.domain.entity.order;

import com.lyf.scm.core.domain.model.order.OrderSequenceDO;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 订单序列号
 * <p>
 * @Author: chuwenchao  2020/7/2
 */
@Data
public class OrderSequenceE extends OrderSequenceDO {

    /**
     * 当前订单序列
     */
    private Long currentSequence;

    /**
     * 当前序列日期
     */
    private Date currentDate;

}
