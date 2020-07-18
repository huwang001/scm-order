package com.lyf.scm.job.config;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Doc:.....
 *
 * @Date: 2019/5/23
 * @Version 1.0
 */
@Configuration
public class JobEventConfig {

	@Resource
	private DataSource dataSource;
	@Bean
	public JobEventConfiguration jobEventConfiguration() {
		return new JobEventRdbConfiguration(dataSource);
	}
}


