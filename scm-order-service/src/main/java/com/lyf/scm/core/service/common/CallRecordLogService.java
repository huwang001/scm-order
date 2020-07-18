package com.lyf.scm.core.service.common;

import com.lyf.scm.core.domain.entity.common.CallRecordLogE;

/**
 * @Description: 回调记录接口对象
 * <p>
 * @Author: wwh 2020/2/21
 */
public interface CallRecordLogService {

    /**
     * @Description: 保存接口调用日志 <br>
     *
     * @Author chuwenchao 2020/3/11
     * @param callRecordLogE
     * @return 
     */
    Integer insertCallRecordLog(CallRecordLogE callRecordLogE);
    
    /**
     * 异步保存外部接口调用日志
     * 
     * @param recordCode
     * @param requestUrl
     * @param requestContent
     * @param responseContent
     * @param status
     */
    void asyncSaveCallRecordLog(String recordCode, String requestUrl, String requestContent, String responseContent, Integer status);
    
}