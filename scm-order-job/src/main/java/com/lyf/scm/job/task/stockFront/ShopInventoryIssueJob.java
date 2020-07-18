package com.lyf.scm.job.task.stockFront;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.stockFront.facade.ShopInventoryRecordServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 门店盘点 Job
 * <p>
 * @Author: lei.jin  2019/5/29
 */
@Slf4j
@Component
public class ShopInventoryIssueJob extends AbstractSimpleJob {

    @Value("${elastic.job.shopInventoryJob.jobName}")
    private String jobName;
    @Value("${elastic.job.shopInventoryJob.cron}")
    private String cron;
    @Value("${elastic.job.shopInventoryJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.shopInventoryJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.shopInventoryJob.failover}")
    private Boolean failover;

    /**
     * 分页查询次数
     */
    private Integer INVENTORY_NUM = 5;

    /**
     * 分页长度
     */
    private Integer INVENTORY_PAGE_SIZE = 200;

    @Resource
    private ShopInventoryRecordServiceFacade shopInventoryRecordServiceFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("批量处理门店盘点单Job启动。。。");
        Long time = System.currentTimeMillis();
        //查询未处理盘点单，1000条
        List<Long> ids = new ArrayList<>();
        for (int i = 1; i <= INVENTORY_NUM; i++) {
            List<Long> inventoryIds = shopInventoryRecordServiceFacade.queryInitShopInventoryRecords((i - 1) * INVENTORY_PAGE_SIZE, INVENTORY_PAGE_SIZE);
            if (inventoryIds != null && inventoryIds.size() > 0) {
                ids.addAll(inventoryIds);
            } else {
                break;
            }
        }
        ids.forEach(id -> {
            try {
                shopInventoryRecordServiceFacade.handleShopInventoryRecords(id);
            } catch (Exception e) {
                log.error("盘点单处理失败 盘点单id{" + id + "}:{}", e);
            }
        });
        time = System.currentTimeMillis() - time;
        log.info("批量处理门店盘点单Job执行成功，耗时 ==> {} ms", time);
    }

    @Override
    public void run(String... args) throws Exception {
        String desc = "批量处理门店盘点单";
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
