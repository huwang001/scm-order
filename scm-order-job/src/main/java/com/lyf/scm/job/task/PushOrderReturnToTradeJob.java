package com.lyf.scm.job.task;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.api.dto.OrderReturnDTO;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.facade.PushOrderReturnToTradeFacade;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 推送退货入库通知给交易中心Job <br>
 *
 * @Author wwh 2020/4/15
 */
@Slf4j
@Component
public class PushOrderReturnToTradeJob extends AbstractSimpleJob {

    @Value("${elastic.job.pushOrderReturnToTradeJob.jobName}")
    private String jobName;
    @Value("${elastic.job.pushOrderReturnToTradeJob.cron}")
    private String cron;
    @Value("${elastic.job.pushOrderReturnToTradeJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.pushOrderReturnToTradeJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.pushOrderReturnToTradeJob.failover}")
    private Boolean failover;
    @Value("${elastic.job.pushOrderReturnToTradeJob.desc}")
    private String desc;

    @Resource
    private PushOrderReturnToTradeFacade pushOrderReturnToTradeFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("推送退货入库通知给交易中心Job启动。。。");
        Long time = System.currentTimeMillis();
        List<OrderReturnDTO> orderReturnDTOList = pushOrderReturnToTradeFacade.queryOrderReturnToTrade();
        if(CollectionUtils.isEmpty(orderReturnDTOList)) {
            return ;
        }
        orderReturnDTOList.forEach(e -> {
        	try {
        		pushOrderReturnToTradeFacade.handleOrderReturnToTrade(e.getAfterSaleCode());
        	} catch (Exception e1) {
			log.error("推送退货[{}]入库通知给交易中心异常", e, e1);
        	}
        });
        time = System.currentTimeMillis() - time;
        log.info("推送退货入库通知给交易中心Job启动执行成功，执行条数 ==> {}, 耗时 ==> {} ms", orderReturnDTOList.size(), time);
    }

    @Override
    public void run(String... args){
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
    
}