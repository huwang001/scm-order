package com.lyf.scm.core.api.controller;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.common.MailSendInfoDTO;
import com.lyf.scm.core.service.common.MailSendInfoService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 邮件发送服务
 * <p>
 * @Author: chuwenchao  2020/4/8
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/mailSend")
@Api(tags={"邮件发送接口管理"})
public class MailController {

    @Resource
    private MailSendInfoService mailSendInfoService;

    @ApiOperation(value = "查找邮箱表中七天内的数据", nickname = "queryMailInfoIntervalSeven", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryMailInfoIntervalSeven", method = RequestMethod.GET)
    public Response<List<MailSendInfoDTO>> queryMailInfoIntervalSeven(){
        try {
            List<MailSendInfoDTO> list = mailSendInfoService.queryMailInfoIntervalSeven();
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "job发送邮件", nickname = "sendMailJob", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/sendMailJob", method = RequestMethod.POST)
    public Response sendMailJob(@RequestBody MailSendInfoDTO messageDTO){
        try {
            mailSendInfoService.sendMailJob(messageDTO);
            return ResponseMsg.SUCCESS.buildMsg();
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
