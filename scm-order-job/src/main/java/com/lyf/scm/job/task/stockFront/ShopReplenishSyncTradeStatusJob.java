package com.lyf.scm.job.task.stockFront;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.common.constants.PageConstants;
import com.lyf.scm.job.api.dto.stockFront.WarehouseRecordPageDTO;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.stockFront.facade.WarehouseRecordServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 门店补货PO通知Job
 * <p>
 * @Author: chuwenchao  2020/6/20
 */
@Slf4j
@Component
public class ShopReplenishSyncTradeStatusJob extends AbstractSimpleJob {

    @Value("${elastic.job.shopReplenishSyncTradeStatusJob.jobName}")
    private String jobName;
    @Value("${elastic.job.shopReplenishSyncTradeStatusJob.cron}")
    private String cron;
    @Value("${elastic.job.shopReplenishSyncTradeStatusJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.shopReplenishSyncTradeStatusJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.shopReplenishSyncTradeStatusJob.failover}")
    private Boolean failover;
    @Value("${elastic.job.shopReplenishSyncTradeStatusJob.desc}")
    private String desc;
    @Resource
    private WarehouseRecordServiceFacade warehouseRecordServiceFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("门店补货单推送交易Job启动。。。");
        long time = System.currentTimeMillis();
        // 查询结果
        List<WarehouseRecordPageDTO> recordList = null;
        int page = 1;
        do {
            recordList = warehouseRecordServiceFacade.queryBySyncTradeStatusByPage((page - 1) * PageConstants.ROWS_200, PageConstants.ROWS_200 );
            if(CollectionUtils.isEmpty(recordList)) {
                break;
            }
            recordList.forEach(record -> {
                try {
                    warehouseRecordServiceFacade.processSyncTradeStatus(record);
                } catch (Exception e) {
                    log.error("单据同步交易中心【{}】异常", record.getRecordCode(), e);
                }
            });
        } while(!CollectionUtils.isEmpty(recordList) && recordList.size() == PageConstants.ROWS_200);
        time = System.currentTimeMillis() - time;
        log.info("门店补货单推送交易Job执行成功，耗时 ==> {} ms", time);
    }

    @Override
    public void run(String... args) throws Exception {
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover,desc);
    }

}
