package com.lyf.scm.core.mapper.order;

import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * com.lyf.scm.core.mapper
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@Mapper
public interface OrderDetailMapper {

    List<OrderDetailE> queryOrderDetailByRecordCode(@Param("orderCode") String orderCode);

    /**
     * 批量插入预约单明细
     * @param orderDetailEList
     * @return
     */
    int batchInsertOrderDetail(@Param("batchList") List<OrderDetailE> orderDetailEList);

    /**
     * 批量更新预约单明细
     * @param orderDetailEList
     * @return
     */
    int batchUpdateOrderDetail(@Param("batchList") List<OrderDetailE> orderDetailEList);

    /**
     * 根据预约单号查询预约单明细
     * @param orderCode
     * @return
     */
    List<OrderDetailE> findByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据预约单号查询预约单明细
     * @param orderCode
     * @return
     */
    List<OrderDetailE> findPageByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据预约单号查询预约单明细列表
     * 
     * @param orderCode
     * @return
     */
	List<OrderDetailE> queryByOrderCode(@Param("orderCode") String orderCode);

}
