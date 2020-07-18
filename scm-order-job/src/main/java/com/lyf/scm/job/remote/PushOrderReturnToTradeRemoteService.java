package com.lyf.scm.job.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.job.api.dto.OrderReturnDTO;
import com.rome.arch.core.clientobject.Response;


/**
 * @Description: 推送退货单给库存中心远程接口 <br>
 *
 * @Author wwh 2020/4/15
 */
@FeignClient(value = "scm-order-service")
public interface PushOrderReturnToTradeRemoteService {
	
	@RequestMapping(value = "/order/v1/orderReturn/queryOrderReturnToTrade", method = RequestMethod.GET)
	Response<List<OrderReturnDTO>> queryOrderReturnToTrade();

	@RequestMapping(value = "/order/v1/orderReturn/handleOrderReturnToTrade", method = RequestMethod.POST)
	Response handleOrderReturnToTrade(@RequestParam(name = "afterSaleCode") String afterSaleCode);

}