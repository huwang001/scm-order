package com.lyf.scm.core.remote.trade;

import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.remote.trade.dto.UpdatePoDTO;
import com.lyf.scm.core.remote.trade.dto.UpdateReversePoDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 交易中心远程接口
 */
@FeignClient(value = "trade-core-inner-server")
public interface TransactionRemoteService {

    /**
     * 推送交易中心(正向PO)
     */
    @RequestMapping(value = "/v1/inner/po/updatePoStatusAndQuantity", method = RequestMethod.POST)
    @ScmCallLog(systemName = "inner-trade", recordCode = "#poUpdateQuantityDTO.doNo")
    Response pushTransactionStatus(@RequestBody UpdatePoDTO poUpdateQuantityDTO);

    /**
     * 门店退货-推送交易中心
     */
    @ScmCallLog(systemName = "inner-trade", recordCode = "#poUpdateQuantityDTO!=null?poUpdateQuantityDTO.doNo:arg0.doNo")
    @RequestMapping(value = "/v1/inner/po/reverse/updateReceivePoStatusAndQuantity", method = RequestMethod.POST)
    Response shopReturnPushTransactionReverseStatus(@RequestBody UpdateReversePoDTO statusAndQuantityDTO);
}
