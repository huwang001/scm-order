package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.dto.*;
import com.lyf.scm.admin.remote.stockFront.dto.BatchCancleDTO;
import com.rome.arch.core.clientobject.Response;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "scm-order-service")
public interface ShopReplenishRemoteService {


    @RequestMapping(value = "/order/v1/shopReplenish/cancelReplenishBatch", method = RequestMethod.POST)
    Response<String> cancelReplenishBatch(@RequestBody BatchCancleDTO dto);

    @RequestMapping(value = "/order/v1/shopReplenish/queryReplenishReportCondition", method = RequestMethod.POST)
    Response<PageInfo<ShopReplenishReportDetailDTO>>  queryReplenishReportCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportCondition condition);

    @RequestMapping(value = "/order/v1/shopReplenish/queryReplenishAllotLogCondition", method = RequestMethod.POST)
    Response<PageInfo<ReplenishAllotLogDTO>> queryReplenishAllotLogCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody ReplenishAllotLogCondition condition);


    @RequestMapping(value = "/order/v1/shopReplenish/statReplenishReport", method = RequestMethod.POST)
    Response<PageInfo<ShopReplenishReportStatDTO>> statReplenishReport(@ApiParam(name = "condition", value = "查询条件") @RequestBody ShopReplenishReportCondition condition);

}
