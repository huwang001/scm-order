package com.lyf.scm.job.remote.shopReturn;

import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 门店退货- 推交易
 *
 * @author zhanglong
 * @date 2020/7/15 21:50
 */
@FeignClient(value = "scm-order-service", url = "http://localhost:8082")
public interface ShopReturnRemoteService {

    /**
     * 获取待推交易的 门店退货单
     *
     * @param page
     * @param maxResult
     * @return
     */
    @RequestMapping(value = "/order/v1/shopReturn/queryUnPushTradeShopReturnRecord", method = RequestMethod.POST)
    Response<List<String>> queryUnPushTradeShopReturnRecord(@RequestParam("page") int page, @RequestParam("maxResult") int maxResult);

    /**
     * 门店退货单-推交易
     *
     * @param frontRecordCode
     * @return
     */
    @RequestMapping(value = "/order/v1/shopReturn/pushTradeShopReturnRecord", method = RequestMethod.POST)
    Response pushTradeShopReturnRecord(@RequestParam("frontRecordCode") String frontRecordCode);
}
