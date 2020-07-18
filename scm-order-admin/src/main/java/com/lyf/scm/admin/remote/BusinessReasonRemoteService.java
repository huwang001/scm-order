package com.lyf.scm.admin.remote;

import com.lyf.scm.admin.remote.stockFront.dto.BusinessReasonDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "scm-order-service")
public interface BusinessReasonRemoteService {

    //根据单据类型查询业务原因
    @RequestMapping(value = "/order/v1/businessReason/queryBusinessReasonByRecordType/{recordType}", method = RequestMethod.GET)
    Response<List<BusinessReasonDTO>> queryBusinessReason(@PathVariable("recordType") Integer recordType);
}
