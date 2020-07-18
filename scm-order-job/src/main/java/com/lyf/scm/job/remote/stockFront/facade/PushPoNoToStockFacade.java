package com.lyf.scm.job.remote.stockFront.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.api.dto.OrderReturnDTO;
import com.lyf.scm.job.remote.stockFront.PushPoNoToStockRemoteService;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 推送PoNo给库存中心 <br>
 *
 * @Author wwh 2020/6/12
 */
@Slf4j
@Component
public class PushPoNoToStockFacade {
	
	@Resource
    private PushPoNoToStockRemoteService pushPoNoToStockRemoteService;
	
	/**
	 * 查询待推送给库存中心的poNo列表
	 * 
	 * @return
	 */
	public List<String> queryPoNoToStock() {
		Response<List<String>> response = pushPoNoToStockRemoteService.queryPoNoToStock();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

	/**
	 * 处理待推送给库存中心的poNo
	 * 
	 * @param recordCode
	 */
	public void handlePoNoToStock(String recordCode) {
		pushPoNoToStockRemoteService.handlePoNoToStock(recordCode);
	}
	
}