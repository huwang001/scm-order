package com.lyf.scm.job.remote.stockFront.facade;

import com.lyf.scm.job.remote.stockFront.ShopAllocationRecordRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 门店调拨
 * @date 2020/6/19
 */
@Slf4j
@Component
public class ShopAllocationRecordServiceFacade {

    @Resource
    private ShopAllocationRecordRemoteService shopAllocationRecordRemoteService;


    /**
     * 批量处理门店调拨推送cmp
     */
    public void handleShopAllocationRecordsPushCmp(){
        shopAllocationRecordRemoteService.handleShopAllocationRecordsPushCmp();
    }
}
