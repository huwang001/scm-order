package com.lyf.scm.core.remote.stock.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.stock.StockReplenishRemoteService;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description: 门店补货
 * <p>
 * @Author: chuwenchao  2020/6/12
 */
@Slf4j
@Component
public class StockReplenishFacade {

    @Resource
    private StockReplenishRemoteService stockReplenishRemoteService;

    /**
     * @Description: 加盟额度扣减通知库存修改PO <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param recordCode
     * @return 
     */
    public void confirmJoinReplenish(String recordCode, String sapPoNo) {
        Response response = stockReplenishRemoteService.confirmJoinReplenish(recordCode, sapPoNo);
        ResponseValidateUtils.validResponse(response);
    }
    
}
