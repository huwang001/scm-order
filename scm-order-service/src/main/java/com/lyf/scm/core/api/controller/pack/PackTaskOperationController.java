package com.lyf.scm.core.api.controller.pack;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.pack.OperationReturnDTO;
import com.lyf.scm.core.api.dto.pack.PackTaskOperationDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishResponseDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.service.pack.PackTaskOperationService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhanglong
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/pack/taskFinish")
@Api(tags = {"包装任务完成清单操作"})
public class PackTaskOperationController {

    @Resource
    private PackTaskOperationService packTaskOperationService;

    @ScmCallLog(systemName = "scm-package-service", recordCode = "#operationList[0].taskCode")
    @ApiOperation(value = "包装任务完成清单同步", nickname = "createPackTaskOperationOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/createPackTaskOperationOrder", method = RequestMethod.POST)
    public Response<List<OperationReturnDTO>> createPackTaskOperationOrder(@ApiParam(name = "operationList", value = "包装任务操作清单同步")
                                                                            @RequestBody @Validated List<PackTaskOperationDTO> operationList) {
        try {
            log.info("包装任务完成清单同步，参数 <<< {}", JSON.toJSONString(operationList));
            List<OperationReturnDTO> returnList = packTaskOperationService.createPackTaskOperationOrder(operationList);
            return Response.builderSuccess(returnList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "包装任务完成清单分页查询", nickname = "queryPackTaskFinishRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/queryPackTaskFinishRecord", method = RequestMethod.POST)
    public Response<PageInfo<TaskFinishResponseDTO>> queryPackTaskFinishRecord(@ApiParam(name = "finishPage", value = "任务完成清单查询") @RequestBody TaskFinishPageDTO finishPage) {
        try {
            PageInfo<TaskFinishResponseDTO> personPageInfo = packTaskOperationService.queryPackTaskFinishRecord(finishPage);
            return Response.builderSuccess(personPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
