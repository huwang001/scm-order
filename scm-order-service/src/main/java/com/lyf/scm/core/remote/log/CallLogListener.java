package com.lyf.scm.core.remote.log;

import cn.hutool.core.util.StrUtil;
import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.service.common.CallRecordLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 异步监听日志事件
 *
 * @author zhangxu
 */
@Slf4j
@AllArgsConstructor
@Component
public class CallLogListener {

    private final CallRecordLogService callRecordLogService;

    @Async
    @Order
    @EventListener(CallLogEvent.class)
    public void saveCallLog(CallLogEvent event) {
        CallRecordLogE callRecordLog = event.getCallRecordLog();
        String requestService = "";
        if (callRecordLog.getRequestUrl().contains("/{")) {
            // 截取uri第一个"/{"之前的字符串
            String uri = StringUtils.substringBefore(callRecordLog.getRequestUrl(), "/{");
            requestService = StringUtils.substringAfterLast(uri, "/");
        } else if (callRecordLog.getRequestUrl().contains("?")) {
            // 截取uri的"?"之前的字符串
            String uri = StringUtils.substringBefore(callRecordLog.getRequestUrl(), "?");
            requestService = StringUtils.substringAfterLast(uri, "/");
        } else {
            // 截取uri最后一个"/"后面的字符串
            requestService = StringUtils.substringAfterLast(callRecordLog.getRequestUrl(), "/");
        }
        callRecordLog.setRequestService(requestService);
        String systemName = callRecordLog.getSystemName();
        if(StringUtils.isEmpty(systemName)) {
            systemName = StrUtil.subBetween(callRecordLog.getRequestUrl(), "/", "/");
        }
        callRecordLog.setSystemName(systemName);
        callRecordLogService.insertCallRecordLog(callRecordLog);
    }
}
