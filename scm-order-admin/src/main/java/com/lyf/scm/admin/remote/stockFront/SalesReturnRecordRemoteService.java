package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.stockFront.dto.SalesReturnRecordParamDTO;
import com.lyf.scm.admin.remote.stockFront.dto.SalesReturnWarehouseRecordDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "scm-order-service")
public interface SalesReturnRecordRemoteService {

    @RequestMapping(value = "/order/v1/salesReturn/condition", method = RequestMethod.POST)
    Response<PageInfo<SalesReturnWarehouseRecordDTO>> findBySalesReturnRecordCondition(@RequestBody SalesReturnRecordParamDTO paramDTO);

    @RequestMapping(value = "/order/v1/salesReturn/queryDetail/{recordId}", method = RequestMethod.GET)
    Response<SalesReturnWarehouseRecordDTO> querySaleReturnWarehouseRecordInfoById(@PathVariable(value = "recordId") Long recordId);
}
