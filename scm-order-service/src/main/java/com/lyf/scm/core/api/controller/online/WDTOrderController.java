package com.lyf.scm.core.api.controller.online;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.service.online.WDTOrderService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/wdtOrder")
@Api(tags = {"旺店通服务接口"})
public class WDTOrderController {


    @Resource
    private WDTOrderService wdtOrderService;

    private ParamValidator validator = ParamValidator.INSTANCE;



    @ApiOperation(value = "根据子do单号取消do单", nickname = "cancelChildDO", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @PostMapping("/cancelChildDo")
    public Response<Boolean> cancelChildDo(@ApiParam(name = "doCode", value = "doCode") @RequestParam String doCode) {
        String message = "";
        boolean isSucc = false;
        try {
            wdtOrderService.cancelChildDo(doCode);
            isSucc = true;
            message = "200";
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            message = e.getMessage();
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            message = e.getMessage();
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        } finally {
//          sapInterfaceLogRepository.saveCallBackInterFaceLog(2, doCode, "wdtCancelChildDO",
//                    doCode, message == null ? "" : message, isSucc);
        }
    }
}
