package com.lyf.scm.core.remote.transport.facade;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.transport.TransportRemoteService;
import com.lyf.scm.core.remote.transport.dto.DoMainDTO;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@AllArgsConstructor
public class TransportFacade {

    @Resource
    private TransportRemoteService transportRemoteService;

    public boolean receiveOutBound(DoMainDTO doMain) {
        if (null != doMain) {
            try {
                log.info("同步出库单到派车系统，单号：{}，入参：{}", doMain.getDoNo(), JSON.toJSONString(doMain));
                Response<Boolean> resp = transportRemoteService.receiveOutBound(doMain);
                log.info("同步出库单到派车系统，单号：{}，出参：{}", doMain.getDoNo(), JSON.toJSONString(resp));
                ResponseValidateUtils.validResponse(resp);
                return resp.getData();
            } catch (Exception e) {
                log.info("调用派车系统接口异常，参数：{}，异常：{}", JSON.toJSONString(doMain), e);
                throw new RomeException(ResCode.ORDER_ERROR_6036, ResCode.ORDER_ERROR_6036_DESC);
            }
        }
        return false;
    }
}
