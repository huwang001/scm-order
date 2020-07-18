package com.lyf.scm.core.remote.trade.facade;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.trade.TransactionRemoteService;
import com.lyf.scm.core.remote.trade.dto.UpdatePoDTO;
import com.lyf.scm.core.remote.trade.dto.UpdateReversePoDTO;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 交易中心接口调用方法
 * @Author zhangtuo
 * @Date 2019/6/28 14:21
 * @Version 1.0
 */
@Slf4j
@Component
public class TransactionFacade {

    @Resource
    private TransactionRemoteService transactionRemoteService;

    /**
     * @Description: 推送交易中心状态 <br>
     *
     * @Author chuwenchao 2020/6/20
     * @param poUpdateQuantityDTO
     * @return 
     */
    public boolean pushTransactionStatus(UpdatePoDTO poUpdateQuantityDTO){
        boolean result = false;
        try {
            Response response = transactionRemoteService.pushTransactionStatus(poUpdateQuantityDTO);
            ResponseValidateUtils.validResponse(response);
            result = true;
        } catch (Exception e) {
            log.info("推送交易门店补货PO异常,参数 ==> {}", JSON.toJSONString(poUpdateQuantityDTO), e);
        }
        return result;
    }

    /**
     * 门店退货-推交易
     * 
     * @author zhanglong
     * @date 2020/7/15 20:01
     */
    public void shopReturnPushTransactionReverseStatus(UpdateReversePoDTO statusAndQuantityDTO) {
        Response response = transactionRemoteService.shopReturnPushTransactionReverseStatus(statusAndQuantityDTO);
        ResponseValidateUtils.validResponse(response);
    }
}
