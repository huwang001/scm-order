package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.*;
import com.lyf.scm.admin.remote.stockFront.ShopReplenishRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.BatchCancleDTO;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShopReplenishFacade {

    @Autowired
    ShopReplenishRemoteService shopReplenishRemoteService;

    public String cancelReplenishBatch(BatchCancleDTO dto) {
        Response<String> response = shopReplenishRemoteService.cancelReplenishBatch(dto);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public PageInfo<ShopReplenishReportDetailDTO> queryReplenishReportCondition(ShopReplenishReportCondition condition) {

        Response<PageInfo<ShopReplenishReportDetailDTO>> response = shopReplenishRemoteService.queryReplenishReportCondition(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public PageInfo<ReplenishAllotLogDTO> queryReplenishAllotLogCondition(ReplenishAllotLogCondition condition) {
        Response<PageInfo<ReplenishAllotLogDTO>> response = shopReplenishRemoteService.queryReplenishAllotLogCondition(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public PageInfo<ShopReplenishReportStatDTO> statReplenishReport(ShopReplenishReportCondition condition) {
        Response<PageInfo<ShopReplenishReportStatDTO>> response = shopReplenishRemoteService.statReplenishReport(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
