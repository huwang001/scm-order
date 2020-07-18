package com.lyf.scm.core.domain.entity.common;

import com.lyf.scm.core.domain.model.common.CallRecordLogDO;

import lombok.Data;

/**
 * @Description: 回调记录日志扩展对象
 * <p>
 * @Author: wwh 2020/2/21
 */
@Data
public class CallRecordLogE extends CallRecordLogDO {
    public CallRecordLogE(String recordCode, String systemName, String requestService, String requestUrl, String requestContent, String responseContent, Integer status) {
        super(recordCode, systemName, requestService, requestUrl, requestContent, responseContent, status);
    }

    public CallRecordLogE() {

    }
}