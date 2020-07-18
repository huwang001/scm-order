package com.lyf.scm.job.remote.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.remote.OrderRemoteService;
import com.lyf.scm.job.remote.dto.OrderDTO;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangxu
 */
@Slf4j
@Component
public class OrderFacade {

    private final OrderRemoteService orderRemoteService;

    public OrderFacade(OrderRemoteService orderRemoteService) {
        this.orderRemoteService = orderRemoteService;
    }

    public void lockInventory(String recordCode) {
        Response<Boolean> resp = orderRemoteService.lockInventory(recordCode);
        ResponseValidateUtils.validResponse(resp);
    }

    public void notifyTradeAfterLocked(OrderDTO orderDTO) {
        Response<Boolean> resp = orderRemoteService.notifyTradeAfterLocked(orderDTO);
        ResponseValidateUtils.validResponse(resp);
    }

    public void notifyTradeDeliveryStatus(OrderDTO orderDTO) {
        Response<Boolean> resp = orderRemoteService.notifyTradeDeliveryStatus(orderDTO);
        ResponseValidateUtils.validResponse(resp);
    }

    public List<OrderDTO> findOrderBySyncTradeStatus(Integer syncTradeStatus) {
        Response<List<OrderDTO>> resp = orderRemoteService.findOrderBySyncTradeStatus(syncTradeStatus);
        ResponseValidateUtils.validResponse(resp);
        return resp.getData();
    }

    /**
     * 查询待锁定的预约单列表
     * 
     * @return
     */
	public List<String> queryWaitLockOrder() {
		Response<List<String>> resp = orderRemoteService.queryWaitLockOrder();
        ResponseValidateUtils.validResponse(resp);
        return resp.getData();
	}
	
}
