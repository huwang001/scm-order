package com.lyf.scm.core.api.controller.notify;

import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.config.RedisUtil;
import com.lyf.scm.core.service.order.OrderUtilService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description: 订单小工具
 * <p>
 * @Author: chuwenchao  2020/7/11
 */
@Slf4j
@RestController
@RequestMapping("/order/v1/orderTool")
@Api(tags={"订单小工具"})
public class OrderToolController {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private OrderUtilService orderUtilService;

    @ApiOperation(value = "查询订单号", nickname = "queryOrderCode", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryOrderCode", method = RequestMethod.GET)
    public Response queryOrderCode(String prefix) {
        try {
            String code = orderUtilService.queryOrderCode(prefix);
            return Response.builderSuccess(code);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "订单小工具one", nickname = "orderToolOne", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/outRecordNotify", method = RequestMethod.POST)
    public Response orderToolOne(String key){
        log.info("订单小工具one，入参 ==> {}", key);
        try {
            redisUtil.del(key);
            return Response.builderSuccess("操作成功");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }


}
