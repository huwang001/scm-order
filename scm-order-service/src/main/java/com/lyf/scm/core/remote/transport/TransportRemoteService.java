package com.lyf.scm.core.remote.transport;

import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.remote.transport.dto.DoMainDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "scm-transport-service")
public interface TransportRemoteService {

    /**
     * 同步出库单 到 派车系统
     *
     * @param doMainDTO
     * @return
     */
    @RequestMapping(value = "/api/v1/doMain/receiveOutBound", method = RequestMethod.POST)
    @ScmCallLog(systemName = "scm-transport-service", recordCode = "#doMainDTO!=null?#doMainDTO.doNo:#arg0.doNo")
    Response<Boolean> receiveOutBound(@RequestBody DoMainDTO doMainDTO);
}
