package com.lyf.scm.job.task.stockFront;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.stockFront.facade.ShopAllocationRecordServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 门店调拨 Job
 * @date 2020/6/19
 */
@Slf4j
@Component
public class ShopAllocationIssueJob extends AbstractSimpleJob {

    @Value("${elastic.job.shopAllocationJob.jobName}")
    private String jobName;
    @Value("${elastic.job.shopAllocationJob.cron}")
    private String cron;
    @Value("${elastic.job.shopAllocationJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.shopAllocationJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.shopAllocationJob.failover}")
    private Boolean failover;
    @Resource
    private ShopAllocationRecordServiceFacade shopAllocationRecordServiceFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("CMP5门店调拨推送cmp调拨单Job启动。。。");
        Long time = System.currentTimeMillis();
        shopAllocationRecordServiceFacade.handleShopAllocationRecordsPushCmp();
        time = System.currentTimeMillis() - time;
        log.info("CMP5门店调拨推送cmp调拨单执行成功，耗时 ==> {} ms", time);
    }

    @Override
    public void run(String... args) throws Exception {
        String desc = "CMP5门店调拨推送cmp调拨单";
        // spring 启动完成后初始化job
//        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
