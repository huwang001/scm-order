package com.lyf.scm.core.mapper.order;

import com.lyf.scm.core.domain.entity.order.OrderVwMoveE;

public interface OrderVwMoveMapper {

    /**
     * @Description: 保存虚库转移单 <br>
     *
     * @Author chuwenchao 2020/4/13
     * @param orderVwMoveE
     * @return 
     */
    int saveOrderVwMove(OrderVwMoveE orderVwMoveE);

}
