package com.lyf.scm.core.mapper.order;

import com.lyf.scm.core.domain.entity.order.OrderSequenceE;

/**
 * @Description: 订单序列表 <br>
 *
 * @Author chuwenchao 2020/7/2
 */
public interface OrderSequenceMapper {

    /**
     * @Description: 查询订单序列 <br>
     *
     * @Author chuwenchao 2020/7/2
     * @return
     */
    OrderSequenceE getOrderSequence();
    
    /**
     * @Description: 更新订单序列 <br>
     *
     * @Author chuwenchao 2020/7/2
     * @param orderSequenceE
     * @return 
     */
    int updateOrderSequenceId(OrderSequenceE orderSequenceE);

}
