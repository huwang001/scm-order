package com.lyf.scm.core.service.order;

/**
 * @Description: 取消预约单接口对象
 * <p>
 * @Author: wwh 2020/4/10
 */
public interface CancelOrderService {

	/**
	 * 根据销售单号取消预约单
	 * 
	 * @param saleCode
	 */
	void cancelOrder(String saleCode);

}