package com.lyf.scm.core.service.common.impl;

import com.lyf.scm.core.domain.entity.common.CallRecordLogE;
import com.lyf.scm.core.mapper.common.CallRecordLogMapper;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lyf.scm.core.service.common.CallRecordLogService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 回调记录接口实现对象
 * <p>
 * @Author: wwh 2020/2/21
 */
@Slf4j
@Service("callRecordLogService")
public class CallRecordLogServiceImpl implements CallRecordLogService {

    @Resource
    private CallRecordLogMapper callRecordLogMapper;

    /**
     * @Description: 保存接口调用日志 <br>
     *
     * @Author chuwenchao 2020/3/11
     * @param callRecordLogE
     * @return
     */
    @Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Integer insertCallRecordLog(CallRecordLogE callRecordLogE) {
        return callRecordLogMapper.insertCallRecordLog(callRecordLogE);
    }

    @Async
	@Override
	public void asyncSaveCallRecordLog(String recordCode, String requestUrl, String requestContent, String responseContent, Integer status) {
		try {
			CallRecordLogE callRecordLogE = new CallRecordLogE();
			callRecordLogE.setRecordCode(recordCode);
			callRecordLogE.setSystemName(StrUtil.subBetween(requestUrl.replaceAll("//", ""), "/", "/"));
			callRecordLogE.setRequestService(StrUtil.split(requestUrl, StrUtil.subBetween(requestUrl.replaceAll("//", ""), "/", "/"))[1]);
			callRecordLogE.setRequestUrl(requestUrl);
			callRecordLogE.setRequestContent(requestContent);
			callRecordLogE.setResponseContent(responseContent);
			callRecordLogE.setStatus(status);
			callRecordLogMapper.insertCallRecordLog(callRecordLogE);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}