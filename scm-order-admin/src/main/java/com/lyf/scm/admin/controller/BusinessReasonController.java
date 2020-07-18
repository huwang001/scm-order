package com.lyf.scm.admin.controller;

import com.lyf.scm.admin.remote.facade.BusinessReasonFacade;
import com.lyf.scm.admin.remote.stockFront.dto.BusinessReasonDTO;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order/v1")
public class BusinessReasonController {

    @Resource
    BusinessReasonFacade businessReasonFacade;

    @ApiOperation(value = "根据单据类型查询业务原因", nickname = "queryBusinessReason", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryBusinessReason", method = RequestMethod.POST)
    public Response queryBusinessReason(@RequestBody Integer recordType) {
        try {
            List<BusinessReasonDTO> reasonDTO = businessReasonFacade.queryBusinessReason(recordType);
            return Response.builderSuccess(reasonDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
