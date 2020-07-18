package com.lyf.scm.core.remote.log;

import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调用接口日志事件
 * @author zhangxu
 */
@Getter
@AllArgsConstructor
public class CallLogEvent {

    private final CallRecordLogE callRecordLog;
}
