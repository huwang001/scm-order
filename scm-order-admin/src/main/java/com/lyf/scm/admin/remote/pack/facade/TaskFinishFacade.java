package com.lyf.scm.admin.remote.pack.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.admin.dto.pack.TaskFinishResponseDTO;
import com.lyf.scm.admin.remote.pack.TaskFinishRemoteService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class TaskFinishFacade {

    @Resource
    private TaskFinishRemoteService taskFinishRemoteService;

    public PageInfo<TaskFinishResponseDTO> queryTaskFinishList(TaskFinishPageDTO pageDTO) {
        Response<PageInfo<TaskFinishResponseDTO>> response = taskFinishRemoteService.queryTaskFinishList(pageDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
