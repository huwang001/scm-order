package com.lyf.scm.job.task.stockFront;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.stockFront.facade.PushPoNoToStockFacade;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 推送PoNo给库存中心Job <br>
 *
 * @Author wwh 2020/6/12
 */
@Slf4j
@Component
public class PushPoNoToStockJob extends AbstractSimpleJob {

    @Value("${elastic.job.pushPoNoToStockJob.jobName}")
    private String jobName;
    @Value("${elastic.job.pushPoNoToStockJob.cron}")
    private String cron;
    @Value("${elastic.job.pushPoNoToStockJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.pushPoNoToStockJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.pushPoNoToStockJob.failover}")
    private Boolean failover;
    @Value("${elastic.job.pushPoNoToStockJob.desc}")
    private String desc;

    @Resource
    private PushPoNoToStockFacade pushPoNoToStockFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("推送PoNo给库存中心Job启动。。。");
        Long time = System.currentTimeMillis();
        List<String> recordCodeList = pushPoNoToStockFacade.queryPoNoToStock();
        if(CollectionUtils.isEmpty(recordCodeList)) {
            return ;
        }
        recordCodeList.forEach(e -> {
        	try {
				pushPoNoToStockFacade.handlePoNoToStock(e);
			} catch (Exception e1) {
				log.error("推送PoNo[{}]给库存中心异常", e, e1);
			}
        });
        time = System.currentTimeMillis() - time;
        log.info("推送PoNo给库存中心Job启动执行成功，执行条数 ==> {}, 耗时 ==> {} ms", recordCodeList.size(), time);
    }

    @Override
    public void run(String... args){
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
    
}