package com.lyf.scm.job.remote.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.api.dto.OrderReturnDTO;
import com.lyf.scm.job.remote.PushOrderReturnToTradeRemoteService;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 推送退货入库通知给交易中心 <br>
 *
 * @Author wwh 2020/4/15
 */
@Slf4j
@Component
public class PushOrderReturnToTradeFacade {

    @Resource
    private PushOrderReturnToTradeRemoteService pushOrderReturnToTradeRemoteService;

    /**
     * 查询待推送给交易中心的退库单列表接口
     * 
     * @return
     */
	public List<OrderReturnDTO> queryOrderReturnToTrade() {
		Response<List<OrderReturnDTO>> response = pushOrderReturnToTradeRemoteService.queryOrderReturnToTrade();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
	}

	/**
	 * 处理待推送给交易中心的退库单接口
	 * 
	 * @param afterSaleCode
	 */
	public void handleOrderReturnToTrade(String afterSaleCode) {
		pushOrderReturnToTradeRemoteService.handleOrderReturnToTrade(afterSaleCode);
	}
	
}
