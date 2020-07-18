package com.lyf.scm.core.service.orderReturn;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnNoticeDTO;

import java.util.List;

/**
 * @Description: 退货单接口对象
 * <p>
 * @Author: wwh 2020/4/8
 */
public interface OrderReturnService {

	/**
	 * 据条件查询退货单列表-分页
	 * 
	 * @param orderReturnDTO
	 * @return
	 */
	PageInfo<OrderReturnDTO> queryOrderReturnPageByCondition(OrderReturnDTO orderReturnDTO);

	/**
	 * 根据售后单号查询退货单详情列表-分页
	 * 
	 * @param afterSaleCode
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	OrderReturnDTO queryOrderReturnDetailPageByAfterSaleCode(String afterSaleCode, Integer pageNum,
			Integer pageSize);
	
	/**
	 * 根据售后单号查询退货单（包含退货单详情）
	 * 
	 * @param afterSaleCode
	 * @return
	 */
	OrderReturnDTO queryOrderReturnWithDetailByAfterSaleCode(String afterSaleCode);
	
	/**
	 * 根据售后单号查询退货单详情列表
	 * 
	 * @param afterSaleCode
	 * @return
	 */
	List<OrderReturnDetailDTO> queryOrderReturnDetailByAfterSaleCode(String afterSaleCode);
	
	/**
	 * 根据销售单号查询退货单列表（包含退货单详情）
	 * 
	 * @param saleCode
	 * @return
	 */
	List<OrderReturnDTO> queryOrderReturnWithDetailBySaleCode(String saleCode);
	
	/**
	 * 接收交易退货单
	 * 
	 * @param returnDTO
	 */
	void receiveReturn(ReturnDTO returnDTO);

	/**
	 * 接收库存中心退货入库通知
	 * 
	 * @param returnNoticeDTO
	 */
	void receiveReturnNotice(ReturnNoticeDTO returnNoticeDTO);
	
	/**
	 * 查询待推送给交易中心的退库单列表
	 * 
	 * @return
	 */
	List<OrderReturnDTO> queryOrderReturnToTrade();

	/**
	 * 处理待推送给交易中心的退库单
	 * 
	 * @param afterSaleCode
	 */
	void handleOrderReturnToTrade(String afterSaleCode);

	/**
	 * 接收库存中心退货入库通知并通知给交易中心
	 * @param stockNotifyDTO
	 */
	void receipt(StockNotifyDTO stockNotifyDTO);

}