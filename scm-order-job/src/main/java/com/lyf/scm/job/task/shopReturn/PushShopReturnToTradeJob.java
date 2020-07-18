package com.lyf.scm.job.task.shopReturn;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.shopReturn.facade.ShopReturnRecordServiceFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 门店退货单 推送给交易 JOB
 *
 * @author zhanglong
 * @date 2020/7/15 21:45
 */
@Slf4j
@Component
public class PushShopReturnToTradeJob extends AbstractSimpleJob {

    @Value("${elastic.job.pushShopReturnToTradeJob.jobName}")
    private String jobName;
    @Value("${elastic.job.pushShopReturnToTradeJob.cron}")
    private String cron;
    @Value("${elastic.job.pushShopReturnToTradeJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.pushShopReturnToTradeJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.pushShopReturnToTradeJob.failover}")
    private Boolean failover;

    /**
     * 分页查询次数
     */
    private Integer PAGE_NUM = 5;

    /**
     * 分页长度
     */
    private Integer PAGE_SIZE = 200;

    @Resource
    private ShopReturnRecordServiceFacade shopReturnRecordServiceFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("门店退货单-推送给交易中心Job启动。。。");
        Long time = System.currentTimeMillis();
        List<String> frontRecordList = new ArrayList<>();
        for (int i = 1; i <= PAGE_NUM; i++) {
            List<String> recordCodes = shopReturnRecordServiceFacade.queryUnPushTradeShopReturnRecord((i - 1) * PAGE_SIZE, PAGE_SIZE);
            if (recordCodes != null && recordCodes.size() > 0) {
                frontRecordList.addAll(recordCodes);
            } else {
                break;
            }
        }
        frontRecordList.forEach(recordCode -> {
            try {
                shopReturnRecordServiceFacade.pushTradeShopReturnRecord(recordCode);
            } catch (Exception e) {
                log.error("门店退货单-同步交易中心【{}】异常", recordCode, e);
            }
        });
        time = System.currentTimeMillis() - time;
        log.info("门店退货单-推送给交易中心Job执行成功，耗时 ==> {} ms", time);
    }

    @Override
    public void run(String... args) throws Exception {
        String desc = "推送门店退货单给交易";
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
