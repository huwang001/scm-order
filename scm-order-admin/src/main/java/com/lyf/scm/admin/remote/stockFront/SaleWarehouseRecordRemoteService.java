package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.SaleWarehouseRecordCondition;
import com.lyf.scm.admin.remote.stockFront.dto.SaleWarehouseRecordDTO;
import com.rome.arch.core.clientobject.Response;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "scm-order-service")
public interface SaleWarehouseRecordRemoteService {

    @RequestMapping(value = "/order/v1/shopSale/list", method = RequestMethod.POST)
    Response<PageInfo<SaleWarehouseRecordDTO>> queryByCondition(@ApiParam(name = "condition", value = "查询条件")
                                                                @RequestBody SaleWarehouseRecordCondition condition);

    @RequestMapping(value = "/order/v1/shopSale/querySaleWarehouseRecordInfo/{recordId}", method = RequestMethod.GET)
    Response<SaleWarehouseRecordDTO> querySaleWarehouseRecordInfoById(@PathVariable(value = "recordId") Long recordId);
}


