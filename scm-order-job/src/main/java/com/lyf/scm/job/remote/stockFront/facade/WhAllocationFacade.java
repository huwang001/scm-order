package com.lyf.scm.job.remote.stockFront.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.job.api.dto.stockFront.WhAllocationDTO;
import com.lyf.scm.job.remote.stockFront.WhAllocationRemoteService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 类WhAllocationFacade的实现描述：调拨facade
 *
 * @author sunyj 2019/7/21 21:44
 */
@Slf4j
@Component
public class WhAllocationFacade {

    @Autowired
    private WhAllocationRemoteService whAllocationRemoteService;

    /**
     * 获取待下发订单
     * @param page
     * @param maxResult
     * @return
     */
    public List<WhAllocationDTO> getWaitSyncOrder(int page, int maxResult){
        Response<List<WhAllocationDTO>> response = whAllocationRemoteService.getWaitSyncOrder(page, maxResult);
        if(ResponseMsg.SUCCESS.getCode().equals(response.getCode())) {
            return response.getData();
        }
        return null;
    }

    /**
     * 下发PO单
     * @param order
     * @return
     */
    public void processWhAllocationOrderToSap(@RequestBody WhAllocationDTO order){
        Response response = whAllocationRemoteService.processWhAllocationOrderToSap(order);
        if (! ResponseMsg.SUCCESS.getCode().equals(response.getCode())) {
            throw new RomeException(ResponseMsg.FAIL.getCode(), response.getMsg());
        }
    }

}
