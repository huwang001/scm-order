package com.lyf.scm.job.remote.shopReturn.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.remote.shopReturn.ShopReturnRemoteService;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 门店退货 推交易
 *
 * @author zhanglong
 * @date 2020/7/15 21:56
 */
@Slf4j
@Component
public class ShopReturnRecordServiceFacade {

    @Resource
    private ShopReturnRemoteService shopReturnRemoteService;

    /**
     * 查询待 推送 交易 的 门店退货单
     */
    public List<String> queryUnPushTradeShopReturnRecord(Integer page, Integer maxResult) {
        Response<List<String>> response = shopReturnRemoteService.queryUnPushTradeShopReturnRecord(page, maxResult);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 门店退货单  推送交易
     */
    public void pushTradeShopReturnRecord(String recordCode) {
        Response<String> response = shopReturnRemoteService.pushTradeShopReturnRecord(recordCode);
        ResponseValidateUtils.validResponse(response);
    }
}
