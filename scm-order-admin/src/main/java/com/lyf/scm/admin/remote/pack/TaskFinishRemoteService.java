package com.lyf.scm.admin.remote.pack;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.admin.dto.pack.TaskFinishResponseDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "scm-order-service")
public interface TaskFinishRemoteService {

    @RequestMapping(value = "/order/v1/pack/taskFinish/queryPackTaskFinishRecord", method = RequestMethod.POST)
    Response<PageInfo<TaskFinishResponseDTO>> queryTaskFinishList(@RequestBody TaskFinishPageDTO pageDTO);
}
