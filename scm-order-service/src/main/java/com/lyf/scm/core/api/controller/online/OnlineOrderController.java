package com.lyf.scm.core.api.controller.online;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ParamValidator;
import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.service.online.OnlineOrderService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 电商相关服务(外接交易中心)
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1")
@Api(tags = {"电商服务接口"})
public class OnlineOrderController {

    @Resource
    private OnlineOrderService onlineOrderService;

    private ParamValidator validator = ParamValidator.INSTANCE;

    /**
     * 电商下单锁定库存
     *
     * @param stockOrderDTO 电商下单相关参数
     * @return
     */
    @ApiOperation(value = "电商下单锁定库存", nickname = "lockStockByRecord", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = String.class)
    @PostMapping("/lockStockByRecord")
    @ScmCallLog(systemName = "inner-trade", recordCode = "#stockOrderDTO.orderCode")
    public Response<Boolean> lockStockByRecord(@ApiParam(name = "stockOrderDTO", value = "电商下单锁定库存") @RequestBody @Validated OnlineOrderDTO stockOrderDTO) {
        if (!validator.validParam(stockOrderDTO)) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        if (!validator.validPositiveInt(stockOrderDTO.getTransType())) {
            return Response.builderFail(ResCode.ORDER_ERROR_1002, ResCode.ORDER_ERROR_1002_DESC);
        }
        //输出kibana日志
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.APP_CALL, "lockStockByRecord", "下单锁定库存, 订单号:" +
                stockOrderDTO.getOrderCode(), JSON.toJSONString(stockOrderDTO)));
        try {
            onlineOrderService.lockStockByRecord(stockOrderDTO);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getMessage(), e);
        }
    }
}
