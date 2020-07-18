package com.lyf.scm.core.api.controller.notify;

import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.notify.TmsNotifyDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.service.notify.TmsNotifyService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: 派车系统回调通知Controller
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
@Slf4j
@RestController
@RequestMapping("/order/v1/tmsNotify")
@Api(tags={"派车回调通知"})
public class TmsNotifyController {

    @Resource
    private TmsNotifyService tmsNotifyService;


    @ApiOperation(value = "创建派车通知", nickname = "dispatchTMSNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "scm-transport-core", recordCode = "#tmsNotifyDTO.recordCode")
    @RequestMapping(value = "/dispatchTMSNotify", method = RequestMethod.POST)
    public Response dispatchTMSNotify(@RequestBody @Valid TmsNotifyDTO tmsNotifyDTO){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.TMS_NOTIFY, "dispatchTMSNotify", "仓库调拨单派车通知（创建）通知", tmsNotifyDTO));
        try {
            tmsNotifyService.dispatchTMSNotify(tmsNotifyDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }



    @ApiOperation(value = "修改派车通知", nickname = "updateDispatchNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "scm-transport-core", recordCode = "#tmsNotifyDTO.recordCode")
    @RequestMapping(value = "/updateDispatchNotify", method = RequestMethod.POST)
    public Response updateDispatchNotify(@RequestBody @Valid TmsNotifyDTO tmsNotifyDTO){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.TMS_NOTIFY, "updateDispatchNotify", "仓库调拨单派车回调（修改）通知", tmsNotifyDTO));
        try {
            if (null == tmsNotifyDTO) {
                return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1003_DESC);
            }
            tmsNotifyService.updateDispatchNotify(tmsNotifyDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


}
