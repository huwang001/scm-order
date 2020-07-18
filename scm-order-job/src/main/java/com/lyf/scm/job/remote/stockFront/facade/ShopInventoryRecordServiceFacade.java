package com.lyf.scm.job.remote.stockFront.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.remote.stockFront.ShopInventoryRecordRemoteService;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 门店盘点
 * @Author lei.jin
 * @Date 2019/6/10 11:37
 * @Version 1.0
 */
@Slf4j
@Component
public class ShopInventoryRecordServiceFacade {

    @Resource
    private ShopInventoryRecordRemoteService shopInventoryRecordRemoteService;

    /**
     * 批量处理门店盘点
     */
    public void handleShopInventoryRecords(Long id) {
        Response<String> response = shopInventoryRecordRemoteService.handleShopInventoryRecords(id);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 分页查询未处理门店盘点单
     *
     * @return
     */
    public List<Long> queryInitShopInventoryRecords(Integer startPage, Integer endPage) {
        Response<List<Long>> response = shopInventoryRecordRemoteService.queryInitShopInventoryRecords(startPage, endPage);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
