package com.lyf.scm.core.service.order;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.order.*;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

/**
 * com.lyf.scm.core.service
 *
 * @author zhangxu
 * @date 2020/4/9
 */
public interface OrderService {

    /**
     * 接收预约单
     *
     * @param tradeOrderDTO
     */
    void receiveTradeOrder(TradeOrderDTO tradeOrderDTO);

    /**
     * 接收交易中心发货通知
     *
     * @param saleCode
     */
    void receiveTradeDeliveryNotice(String saleCode);

    /**
     * 完全锁定后通知交易中心
     */
    int notifyTradeAfterLocked(OrderDTO orderDTO);

    /**
     * 查询预约单
     *
     * @param orderCode
     */
    OrderE queryOrderByCode(String orderCode);

    /**
     * 查询预约单明细
     *
     * @param orderCode
     */
    List<OrderDetailE> queryOrderDetailsByCode(String orderCode);

    /**
     * 完成加工
     */
    void completeProcess(String orderCode, Long userId);

    /**
     * 一次锁虚仓库存
     */
    void lockInventory(String recordCode);

    /**
     * 二次锁库存
     */
    void lockSeondary(String orderCode);

    /**
     * 通知交易中心发货状态
     */
    int notifyTradeDeliveryStatus(OrderDTO orderDTO);

    /**
     * 根据销售单号查询预约单锁定状态
     */
    List<OrderDetailLockStatusDTO> queryOrderDetailLockStatus(String saleCode);


    List<OrderDetailRespDTO> queryOrderDetailByOrderCode(String orderCode);

    /**
     * 创建DO出库单
     * @param orderCode
     */
    void createDo(String orderCode, Long userId);

    /**
     * 强制生成DO
     * @param orderCode
     * @param userId
     */
    void forceCreateDo(String orderCode, Long userId);

    PageInfo<OrderRespDTO> pageOrder(QueryOrderDTO queryOrderDTO, Integer pageNum, Integer pageSize);

    List<OrderRespDTO> findOrder(QueryOrderDTO queryOrderDTO);

    OrderRespDTO findOrder(String orderCode);

    PageInfo<OrderDetailRespDTO> pageOrderDetail(String orderCode, Integer pageNum, Integer pageSize);
    
    
    /**
     * 根据预约单号查询预约单（包含明细）
     * 
     * @param orderCode
     * @return
     */
    OrderE queryOrderWithDetail(String orderCode);

    /**
     * 预约单出库回调
     * @param stockNotifyDTO
     */
    void orderOutNotify(StockNotifyDTO stockNotifyDTO);
    
    /**
     * 调拨出库通知
     * 
     * @param whAllocationE
     */
    void allotOutNotify(WhAllocationE whAllocationE);
    
    /**
     * 调拨入库通知
     * 
     * @param whAllocationE
     */
    void allotInNotify(WhAllocationE whAllocationE);
    
    /**
     * 查询待锁定的预约单列表
     * 
     * @return
     */
	List<String> queryWaitLockOrder();

	/**
	 * 根据销售单号查询预约单及明细
	 * 
	 * @param orderCode
	 * @return
	 */
	List<OrderDetailRespDTO> findByOrderCode(String orderCode);

	/**
	 * 根据同步交易状态查询预约单
	 * 
	 * @param syncTradeStatus
	 * @param monthAgo
	 * @return
	 */
	List<OrderDTO> findBySyncTradeStatus(Integer syncTradeStatus, Date monthAgo);

	/**
	 * 根据销售单号查询预约单
	 * 
	 * @param saleCode
	 * @return
	 */
	OrderRespDTO findBySaleCode(String saleCode);

	/**
	 * 根据销售单号查询预约单及明细
	 * 
	 * @param saleCode
	 * @return
	 */
	OrderRespDTO queryOrderAndOrderDetailBySaleCode(String saleCode);
	
	/**
	 * 根据预约单号修改单据状态、同步交易状态
	 * 
	 * @param recordCode
	 */
	void updateOrderStatusAndSyncTradeStatus(String recordCode);

    /**
     * @Description 预约单页面创建需求单
     * @author huwang
     * @Date 2020/7/15 19:35
     * @param orderCreatePackDemandDTO
     * @return void
     **/
    void createPackDemand(OrderCreatePackDemandDTO orderCreatePackDemandDTO);

}
