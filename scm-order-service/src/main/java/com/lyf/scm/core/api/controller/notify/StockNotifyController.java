package com.lyf.scm.core.api.controller.notify;

import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.enums.ResponseMsg;
import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.service.notify.StockNotifyService;
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
 * @Description: 库存回调通知Controller
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
@Slf4j
@RestController
@RequestMapping("/order/v1/stockNotify")
@Api(tags={"库存回调通知"})
public class StockNotifyController {

    @Resource
    private StockNotifyService stockNotifyService;

    @ApiOperation(value = "wms出库结果通知", nickname = "outRecordNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "stock-core", recordCode = "#stockNotifyDTO!=null?#stockNotifyDTO.recordCode:#arg0.recordCode")
    @RequestMapping(value = "/outRecordNotify", method = RequestMethod.POST)
    public Response outRecordNotify(@RequestBody @Valid StockNotifyDTO stockNotifyDTO){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.STOCK_NOTIFY, "outRecordNotify", "wms出库结果通知", stockNotifyDTO));
        try {
            stockNotifyService.outRecordNotify(stockNotifyDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "wms入库结果通知", nickname = "inRecordNotify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @ScmCallLog(systemName = "stock-core", recordCode = "#stockNotifyDTO!=null?#stockNotifyDTO.recordCode:#arg0.recordCode")
    @RequestMapping(value = "/inRecordNotify", method = RequestMethod.POST)
    public Response inRecordNotify(@RequestBody @Valid StockNotifyDTO stockNotifyDTO){
        log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.STOCK_NOTIFY, "inRecordNotify", "wms入库结果通知", stockNotifyDTO));
        try {
            stockNotifyService.inRecordNotify(stockNotifyDTO);
            return Response.builderSuccess(null);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return ResponseMsg.FAIL.buildMsg(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
