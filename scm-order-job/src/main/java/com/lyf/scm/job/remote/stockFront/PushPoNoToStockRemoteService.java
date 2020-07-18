package com.lyf.scm.job.remote.stockFront;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.rome.arch.core.clientobject.Response;


/**
 * @Description: 推送PoNo给库存中心远程接口 <br>
 *
 * @Author wwh 2020/6/12
 */
@FeignClient(value = "scm-order-service")
public interface PushPoNoToStockRemoteService {
	
	@RequestMapping(value = "/order/v1/wh_allocation/queryPoNoToStock", method = RequestMethod.GET)
	Response<List<String>> queryPoNoToStock();

	@RequestMapping(value = "/order/v1/wh_allocation/handlePoNoToStock", method = RequestMethod.POST)
	Response handlePoNoToStock(@RequestParam(name = "recordCode") String recordCode);

}