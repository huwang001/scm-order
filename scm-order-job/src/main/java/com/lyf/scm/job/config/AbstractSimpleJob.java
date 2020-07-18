package com.lyf.scm.job.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;

/**
 * @Doc:SimpleJob抽象类
 *
 * @Date: 2019/5/22
 * @Version 1.0
 */
public abstract class AbstractSimpleJob implements SimpleJob, CommandLineRunner {
	@Resource
	protected ZookeeperRegistryCenter regCenter;
	@Resource
	protected ElasticJobListener elasticJobListener;
	@Resource
	private JobEventConfiguration jobEventConfiguration;

	protected void registerSimpleJob(String jobName, String cron, int shardingTotalCount,
									 String shardingItemParameters, boolean failover, String desc) {
		new SpringJobScheduler(this, regCenter, getLiteJobConfiguration(this.getClass(), jobName, cron,
				shardingTotalCount, shardingItemParameters, failover, desc),jobEventConfiguration, elasticJobListener).init();
	}

	/**
	 * @param jobClass               实现SimpleJob接口的实例
	 * @param jobName                定时任务名称
	 * @param cron                   任务启动时间 格式 "0/20 * * * * ?"
	 * @param shardingTotalCount     分片数
	 * @param shardingItemParameters 任务参数 ，例子：0=A,1=B
	 * @param desc
	 * @return
	 */
	private static LiteJobConfiguration getLiteJobConfiguration(Class<? extends SimpleJob> jobClass, String jobName,
																String cron, int shardingTotalCount, String shardingItemParameters, boolean failover, String desc) {
		return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
				JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount)
						.jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), RomeJobExceptionHandler.class.getCanonicalName())
						.failover(failover)
						.description(desc)
						.shardingItemParameters(shardingItemParameters).jobParameter(shardingItemParameters).build(),
				jobClass.getCanonicalName())).overwrite(true).build();
	}
}
