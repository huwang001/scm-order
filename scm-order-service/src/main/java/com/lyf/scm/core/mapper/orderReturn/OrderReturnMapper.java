package com.lyf.scm.core.mapper.orderReturn;

import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDTO;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 退货单Mapper <br>
 *
 * @Author wwh 2020/4/8
 */
public interface OrderReturnMapper {
	

	/**
	 * 保存退货单
	 * 
	 * @param orderReturnE
	 * @return
	 */
    int insertOrderReturn(OrderReturnE orderReturnE);

    /**
     * 修改退货单
     * 
     * @param orderReturnE
     * @return
     */
    int updateOrderReturn(OrderReturnE orderReturnE);

    /**
     * 根据条件查询退货单列表
     * 
     * @param orderReturnDTO
     * @return
     */
    List<OrderReturnE> queryOrderReturnByCondition(OrderReturnDTO orderReturnDTO);

    /**
     * 根据售后单查询退货单
     * 
     * @param afterSaleCode
     * @return
     */
    OrderReturnE queryOrderReturnByAfterSaleCode(@Param("afterSaleCode") String afterSaleCode);

    /**
     * 根据销售单号查询退货单列表
     * 
     * @param saleCode
     * @return
     */
	List<OrderReturnE> queryOrderReturnBySaleCode(@Param("saleCode") String saleCode);

	/**
	 * 根据销售单号查询售后单号列表
	 * 
	 * @param saleCode
	 * @return
	 */
	List<Map<String, String>> queryAfterSaleCodeListBySaleCode(@Param("saleCode") String saleCode);

	/**
	 * 根据售后单号修改团购入库单号、通知库存中心状态=2（已通知）
	 * 
	 * @param afterSaleCode
	 * @param returnEntryCode
	 * @return
	 */
	int updateSyncStockStatusAndReturnEntryCode(@Param("afterSaleCode") String afterSaleCode, @Param("returnEntryCode") String returnEntryCode, @Param("status") Integer status);

	/**
	 * 根据售后单号修改通知库存中心状态=1（待通知）
	 * 
	 * @param afterSaleCode
	 * @return
	 */
	int updateSyncStockStatus(@Param("afterSaleCode") String afterSaleCode);

	/**
	 * 根据售后单号修改退货单单据状态=2（已入库）
	 * 
	 * @param afterSaleCode
	 * @return
	 */
	int updateOrderStatusByAfterSaleCode(@Param("afterSaleCode") String afterSaleCode);
	
	/**
	 * 根据售后单号、状态修改通知交易中心状态
	 * 
	 * @param afterSaleCode
	 * @param status
	 * @return
	 */
	int updateSyncTradeStatus(@Param("afterSaleCode") String afterSaleCode, @Param("status") Integer status, @Param("oldStatus") Integer oldStatus);

	/**
	 * 查询待推送给交易中心的退库单列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OrderReturnE> queryOrderReturnToTrade(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据单据编号查询退货单
	 * @param recordCode
	 * @return
	 */
	OrderReturnE queryReturnByRecordCode(@Param("recordCode") String recordCode);

	/**
	 * 根据ID查询退货单
	 * @param idList
	 * @return
	 */
    List<OrderReturnE> queryOrderReturnByIds(@Param("idList") List<Long> idList);
}