package com.lyf.scm.admin.remote.pack;

import com.lyf.scm.admin.dto.pack.DemandAllotDTO;
import com.lyf.scm.admin.dto.pack.PackDemandComponentDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(value = "scm-order-service")
public interface PackDemandComponentService {

    @RequestMapping(value = "/order/v1/pack/demandComponent/createDemandAllot", method = RequestMethod.POST)
    Response createDemandAllot(@RequestBody DemandAllotDTO demandAllotDTO);

    /**
     * 调拨时根据需求编码查询需求单明细原料列表
     * @param recordCode
     * @return
     */
    @GetMapping(value = "/order/v1/pack/demandComponent/queryDemandComponentByRecordCodeAllot")
    Response<List<PackDemandComponentDTO>> queryDemandComponentByRecordCodeAllot(@RequestParam("recordCode") String recordCode);
}