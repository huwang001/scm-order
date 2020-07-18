package com.lyf.scm.core.mapper.order;

import com.lyf.scm.core.domain.entity.order.OrderVwMoveDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderVwMoveDetailMapper {

    /**
     * @Description: 批量保存虚仓转移单明细 <br>
     *
     * @Author chuwenchao 2020/4/13
     * @param detailEList
     * @return 
     */
    int batchSaveOrderVwMoveDetail(@Param("detailEList") List<OrderVwMoveDetailE> detailEList);
}
