package com.lyf.scm.admin.remote;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.OrderReturnDTO;
import com.lyf.scm.admin.remote.dto.OrderReturnDetailDTO;
import com.rome.arch.core.clientobject.Response;

/**
 * @Description: 退货单管理远程接口 <br>
 *
 * @Author wwh 2020/4/16
 */
@FeignClient(value="scm-order-service")
public interface OrderReturnRemoteService {

	@RequestMapping(value = "/order/v1/orderReturn/queryOrderReturnPageByCondition", method = RequestMethod.POST)
	Response<PageInfo<OrderReturnDTO>> queryOrderReturnPageByCondition(@RequestBody OrderReturnDTO orderReturnDTO);
	
	@RequestMapping(value = "/order/v1/orderReturn/queryOrderReturnDetailPageByAfterSaleCode", method = RequestMethod.POST)
	Response<OrderReturnDTO> queryOrderReturnDetailPageByAfterSaleCode(@RequestParam("afterSaleCode") String afterSaleCode, @RequestParam("pageNum") Integer pageNum,
    		@RequestParam("pageSize") Integer pageSize);

	@RequestMapping(value = "/order/v1/orderReturn/queryOrderReturnWithDetailByAfterSaleCode/{afterSaleCode}", method = RequestMethod.GET)
	Response<OrderReturnDTO> queryOrderReturnWithDetailByAfterSaleCode(@PathVariable("afterSaleCode") String afterSaleCode);

	@RequestMapping(value = "/order/v1/orderReturn/queryOrderReturnDetailByAfterSaleCode/{afterSaleCode}", method = RequestMethod.GET)
	Response<List<OrderReturnDetailDTO>> queryOrderReturnDetailByAfterSaleCode(@PathVariable("afterSaleCode") String afterSaleCode);
	
}