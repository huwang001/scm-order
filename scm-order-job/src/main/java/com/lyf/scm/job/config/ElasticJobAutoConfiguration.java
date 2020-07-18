
package com.lyf.scm.job.config;

import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Doc:zk配置
 *
 * @Date: 2019/5/22
 * @Version 1.0
 */
@Configuration
public class ElasticJobAutoConfiguration {

	@Value("${elaticjob.zookeeper.server-lists}")
	private String serverLists;
	
	@Value("${elaticjob.zookeeper.namespace}")
	private String namespace;
	
	@Value("${elaticjob.zookeeper.sessiontimeout}")
	private Integer zkSessionTimeout;

	@Bean(initMethod = "init")
	public ZookeeperRegistryCenter zookeeperRegistryCenter(){
		 ZookeeperConfiguration config = new ZookeeperConfiguration(serverLists, namespace);
	        config.setSessionTimeoutMilliseconds(zkSessionTimeout);
	        ZookeeperRegistryCenter regCenter = new ZookeeperRegistryCenter(config);
	        return regCenter;
	}

	@Bean
	public ElasticJobListener elasticJobListener() {
		return new ScmElasticJobListener();
	}
}
