package com.lyf.scm.job.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.common.constants.OrderConstants;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.dto.OrderDTO;
import com.lyf.scm.job.remote.facade.OrderFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 锁定库存
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@Slf4j
@Component
public class LockInventoryJob extends AbstractSimpleJob {

    @Value("${elastic.job.lockInventoryJob.jobName}")
    private String jobName;
    @Value("${elastic.job.lockInventoryJob.cron}")
    private String cron;
    @Value("${elastic.job.lockInventoryJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.lockInventoryJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.lockInventoryJob.failover}")
    private Boolean failover;
    @Value("${elastic.job.lockInventoryJob.desc}")
    private String desc;

    @Resource
    private OrderFacade orderFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("锁定库存Job启动。。。");
        long time = System.currentTimeMillis();
        List<String> recordCodes = orderFacade.queryWaitLockOrder();
        for (String recordCode : recordCodes) {
            try {
                orderFacade.lockInventory(recordCode);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("锁定库存Job启动执行成功，执行条数 ==> {}, 耗时 ==> {} ms", recordCodes.size(), System.currentTimeMillis() - time);
    }

    @Override
    public void run(String... args) throws Exception {
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
