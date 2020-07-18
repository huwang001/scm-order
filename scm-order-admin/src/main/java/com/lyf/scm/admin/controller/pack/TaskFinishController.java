package com.lyf.scm.admin.controller.pack;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.admin.dto.pack.TaskFinishResponseDTO;
import com.lyf.scm.admin.remote.pack.facade.TaskFinishFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 包装需求-任务完成清单
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/taskFinish")
@Api(tags = {"任务完成清单"})
public class TaskFinishController {

    @Resource
    private TaskFinishFacade taskFinishFacade;

    @ApiOperation(value = "任务完成清单-分页查询", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @PostMapping("/queryTaskFinishList")
    public Response<PageInfo<TaskFinishResponseDTO>> queryTaskFinishList(@ApiParam(name = "pageDTO", value = "分页查询对象")
                                                                         @RequestBody @Valid TaskFinishPageDTO pageDTO) {
        try {
            PageInfo<TaskFinishResponseDTO> pageInfo = taskFinishFacade.queryTaskFinishList(pageDTO);
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
