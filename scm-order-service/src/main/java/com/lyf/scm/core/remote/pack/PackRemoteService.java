package com.lyf.scm.core.remote.pack;

import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.remote.pack.dto.CancelRequireDTO;
import com.lyf.scm.core.remote.pack.dto.ObtainOrderDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Desc:包装系统对外接口
 * @author:Huangyl
 * @date: 2020/7/7
 */
@FeignClient(value = "scm-package-service")
public interface PackRemoteService {

    /**
     * 需求单下发包装系统
     *
     * @param obtainOrderDTO
     */
    @ScmCallLog(systemName = "scm-package-service", recordCode = "#obtainOrderDTO!=null?#obtainOrderDTO.demandNo:#arg0.demandNo")
    @RequestMapping(value = "/external/obtainOrder", method = RequestMethod.POST)
    Response releasePackSystem(@RequestBody ObtainOrderDTO obtainOrderDTO);

    /**
     * 批量取消需求单
     *
     * @param requireCodeList
     * @return
     */
    @ScmCallLog(systemName = "scm-package-service", recordCode = "#requireCodeList!=null?#requireCodeList[0]:#arg0[0]")
    @RequestMapping(value = "/external/cancelRequireOrder", method = RequestMethod.POST)
    Response<List<CancelRequireDTO>> cancelRequireOrder(@RequestBody List<String> requireCodeList);
}
