package com.lyf.scm.core.mapper.order;

import com.lyf.scm.core.api.dto.order.QueryOrderDTO;
import com.lyf.scm.core.api.dto.order.UpdateOrderDTO;
import com.lyf.scm.core.domain.entity.order.OrderE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * com.lyf.scm.core.mapper
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@Mapper
public interface OrderMapper {

    OrderE queryOrderByRecordCode(@Param("orderCode") String orderCode);

    int updateOrderAllocationStatus(OrderE orderE);

    /**
     * 根据调拨单号allotCode查询预约单
     * @param allotCode
     * @return
     */
    OrderE queryOrderByAllotCode(String allotCode);

    int updateOrderAllocationOutStatusByRecordCode(@Param("orderCode") String orderCode);

    int updateOrderAllocationInStatusByRecordCode(@Param("orderCode") String orderCode);

    /**
     * 插入预约单
     * @param orderE
     * @return
     */
    int insertOrder(OrderE orderE);

    /**
     * 更新预约单
     * @param orderDTO
     * @return
     */
    int updateOrder(OrderE orderE);

    /**
     * 根据页面查询条件查询预约单
     * @param queryOrderDTO
     * @return
     */
    List<OrderE> findByCondition(QueryOrderDTO queryOrderDTO);
    /**
     * 根据页面查询条件查询预约单
     * @param queryOrderDTO
     * @return
     */
    List<OrderE> findPageByCondition(QueryOrderDTO queryOrderDTO);

    /**
     * 根据预约单号查询预约单
     * @param orderCode
     * @return
     */
    OrderE findByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据销售单号查询预约单
     * @param saleCode
     * @return
     */
    OrderE findBySaleCode(@Param("saleCode") String saleCode);

    /**
     * 更新预约单状态为加工完成
     * @param orderCode
     * @return
     */
    int updateOrderStatusProcessCompletedByOrderCode(@Param("orderCode") String orderCode, @Param("userId") Long userId);

    /**
     * 更新预约单状态为待发货(需要包装)
     * @param orderCode
     * @return
     */
    int updateOrderStatusDeliveryWaitWhenNeedPackageByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 更新预约单状态为待发货（不需要包装）
     * @param orderCode
     * @return
     */
    int updateOrderStatusDeliveryWaitWhenNonNeedPackageByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 更新预约单状态为已发货
     * @param orderCode
     * @return
     */
    int updateOrderStatusDeliveryDoneByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 更新预约单交易审核状态为通过
     * @param orderCode
     * @return
     */
    int updateTradeAuditStatusPassedByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 更新预约单同步交易中心状态已同步（锁定）
     * @param orderCode
     * @return
     */
    int updateSyncTradeStatusLockDoneByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 更新预约单同步交易中心状态待同步（锁定）
     * @param orderCode
     * @return
     */
    int updateSyncTradeStatusLockWaitByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 更新预约单同步交易中心状态待同步（DO）
     * @param orderCode
     * @return
     */
    int updateSyncTradeStatusDoWaitByOrderCode(@Param("orderCode") String orderCode);
    /**
     * 更新预约单同步交易中心状态已同步（DO）
     * @param orderCode
     * @return
     */
    int updateSyncTradeStatusDoDoneByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据预约单同步交易状态查询
     * @param syncTradeStatus
     * @param createTime
     * @return
     */
    List<OrderE> findBySyncTradeStatus(@Param("syncTradeStatus") Integer syncTradeStatus, @Param("createTime") Date createTime);

    /**
     * 根据do单号查询预约单
     * @param doCode
     * @return
     */
    OrderE findByDoCode(@Param("doCode") String doCode);

	/**
     * 根据销售单号查询预约单
     *
     * @param saleCode
     * @return
     */
	OrderE queryOrderBySaleCode(@Param("saleCode") String saleCode);


    /**
     * 更新发货状态为已发货
     * @param orderCode
     * @return
     */
    int updateOrderDeliveryStatusByRecordCode(String orderCode);
    
    /**
     * 更新预约单交易审核状态为已取消
     * 
     * @param orderCode
     * @return
     */
    int updateOrderStatusCancelByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据预约单号查询预约单
     * 
     * @param orderCode
     * @return
     */
	OrderE queryByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据预约单ID查询预约单
     * @param orderId   预约单ID
     * @return
     */
    OrderE queryOrderById(@Param("orderId") Long orderId);

    /**
     *  根据预约单号修改预约单状态(改为31已发货)
     * @param orderCode 预约单号
     * @return
     */
    int updateOrderStatusByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 创建出库单后更新是否创建出库单状态为已创建
     * @param orderCode
     * @return
     */
    int updateHasNoByOrderCode(@Param("orderCode") String orderCode);

    /**
     * 根据销售编号
     * 修改预约单为已加工
     * @param saleCode
     * @return
     */
    int updateOrderStatusBySaleCode(@Param("saleCode") String saleCode);

    /**
     * 根据idList查询预约单
     * @param idList
     * @return
     */
    List<OrderE> queryOrderByIds(@Param("idList") List<Long> idList);

    /**
     * 查询待锁定的预约单列表
     * 
     * @param startTime
     * @param endTime
     * @return
     */
	List<String> queryWaitLockOrder(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
	
	/**
	 * 根据ID修改预约单锁定状态
	 * 
	 * @param id
	 * @param orderStatus
	 * @param versionNo
	 * @return
	 */
	int updateLockStatus(@Param("id") Long id, @Param("orderStatus") Integer orderStatus, @Param("versionNo") Integer versionNo);

    /**
     * 更新预约单同步交易中心状态待同步（DO）
     * @param orderCode
     * @return
     */
    int updateSyncTradeStatusByOrderCode(String orderCode);

    /**
     * 根据ID修改单据状态、同步交易状态（待同步-锁定）（orderStatus=0初始下单）
     * 
     * @param recordCode
     * @param status
     * @param versionNo
     * @return
     */
	int updateOrderStatusAndSyncTradeStatus(@Param("id") Long id, @Param("orderStatus") Integer orderStatus,@Param("oldOrderStatus") Integer oldOrderStatus, @Param("versionNo") Integer versionNo);

    /**
     * @Description 更新预约单的仓库信息、交易状态和单据状态
     * @author huwang
     * @Date 2020/7/16 9:57
     * @param updateOrderDTO
     * @return int
     **/
    int updateOrderByCondition(UpdateOrderDTO updateOrderDTO);
}
