package com.lyf.scm.job.config;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Doc:监控配置，任务启动前后可以做一些事情
 *
 * @Date: 2019/5/21
 * @Version 1.0
 */
@Component
@Slf4j
public class ScmElasticJobListener implements ElasticJobListener {

	private long beginTime = 0;
	@Override
	public void beforeJobExecuted(ShardingContexts shardingContexts) {
		//beginTime = System.currentTimeMillis();
		//log.info("=======================================gaa=START===========================================");
		//log.info("{} JOB BEGIN TIME: {} ",shardingContexts.getJobName(), new Date(beginTime));
	}

	@Override
	public void afterJobExecuted(ShardingContexts shardingContexts) {
		//long endTime = System.currentTimeMillis();
		//log.info("{} JOB END TIME: {},TOTAL CAST: {} ",shardingContexts.getJobName(), new Date(beginTime), endTime - beginTime);
		//log.info("========================================END===========================================");
	}

}
