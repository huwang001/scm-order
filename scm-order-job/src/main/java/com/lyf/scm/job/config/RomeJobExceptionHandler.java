package com.lyf.scm.job.config;

import com.dangdang.ddframe.job.executor.handler.JobExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Doc:异常处理
 *
 * @Date: 2019/5/22
 * @Version 1.0
 */
@Slf4j
public class RomeJobExceptionHandler implements JobExceptionHandler {

	@Override
	public void handleException(String jobName, Throwable cause) {
		// Auto-generated method stub
		log.error("任务名称 :{} 执行失败,原因 : {},请关注! {}", jobName, cause.getLocalizedMessage(), cause);
	}

}
