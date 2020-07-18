package com.lyf.scm.core.api.controller;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.api.dto.common.RecordStatusLogDTO;
import com.lyf.scm.core.service.common.RecordStatusLogService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 单据状态流转日志Controller
 * <p>
 * @Author: wwh 2020/3/10
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/recordStatusLog")
@Api(tags={"单据状态流转日志接口管理"})
public class RecordStatusLogController {
	
	@Resource
    private RecordStatusLogService recordStatusLogService;
	
	@ApiOperation(value = "通过预约单号查询状态记录", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryRecordStatusLogByOrderCode", method = RequestMethod.POST)
    public Response<List<RecordStatusLogDTO>> queryRecordStatusLogByOrderCode(@ApiParam(name = "orderCode", value = "预约单号") @RequestParam(name = "orderCode") String orderCode) {
        try {
            List<RecordStatusLogDTO> recordStatusLogDTOList = recordStatusLogService.queryRecordStatusLogByOrderCode(orderCode);
            return Response.builderSuccess(recordStatusLogDTOList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
