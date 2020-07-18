package com.lyf.scm.job.task.stockFront;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.stockFront.facade.WarehouseRecordServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 同步派车系统 Job
 */
@Slf4j
@Component
public class SynDispatchCarJob extends AbstractSimpleJob {

    @Value("${elastic.job.synDispatchCarJob.jobName}")
    private String jobName;
    @Value("${elastic.job.synDispatchCarJob.cron}")
    private String cron;
    @Value("${elastic.job.synDispatchCarJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.synDispatchCarJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.synDispatchCarJob.failover}")
    private Boolean failover;

    /**
     * 分页查询次数
     */
    private Integer NUM = 5;

    /**
     * 分页长度
     */
    private Integer PAGE_SIZE = 200;

    @Resource
    private WarehouseRecordServiceFacade warehouseRecordServiceFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("批量同步出库单到派车系统Job启动。。。");
        Long time = System.currentTimeMillis();
        //查询未处理盘点单，1000条
        List<Long> ids = new ArrayList<>();
        for (int i = 1; i <= NUM; i++) {
            List<Long> warehouseRecordIds = warehouseRecordServiceFacade.queryNeedSyncTmsBWarehouseRecords((i - 1) * PAGE_SIZE, PAGE_SIZE);
            if (warehouseRecordIds != null && warehouseRecordIds.size() > 0) {
                ids.addAll(warehouseRecordIds);
            } else {
                break;
            }
        }
        ids.forEach(id -> {
            try {
                warehouseRecordServiceFacade.handleDispatchCarWarehouseRecord(id);
            } catch (Exception e) {
                log.error("同步派车系统处理失败 出入库单id{" + id + "}:{}", e);
            }
        });
        time = System.currentTimeMillis() - time;
        log.info("批量同步出库单到派车系统Job执行成功，耗时 ==> {} ms", time);
    }

    @Override
    public void run(String... args) throws Exception {
        String desc = "批量同步出库单到派车系统";
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
