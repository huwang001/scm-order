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
 * 通知交易中心发货状态
 *
 * @author zhangxu
 * @date 2020/4/14
 */
@Slf4j
@Component
public class NotifyTradeDeliveryStatusJob extends AbstractSimpleJob {

    @Value("${elastic.job.notifyTradeDeliveryStatusJob.jobName}")
    private String jobName;
    @Value("${elastic.job.notifyTradeDeliveryStatusJob.cron}")
    private String cron;
    @Value("${elastic.job.notifyTradeDeliveryStatusJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.notifyTradeDeliveryStatusJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.notifyTradeDeliveryStatusJob.failover}")
    private Boolean failover;
    @Value("${elastic.job.notifyTradeDeliveryStatusJob.desc}")
    private String desc;

    @Resource
    private OrderFacade orderFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("通知交易中心发货状态Job启动。。。");
        long time = System.currentTimeMillis();
        List<OrderDTO> orderDTOS = orderFacade.findOrderBySyncTradeStatus(OrderConstants.SYNC_TRADE_STATUS_DO_WAIT);
        if (CollectionUtils.isEmpty(orderDTOS)) {
            return;
        }
        for (OrderDTO orderDTO : orderDTOS) {
            try {
                orderFacade.notifyTradeDeliveryStatus(orderDTO);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("通知交易中心发货状态Job启动执行成功，执行条数 ==> {}, 耗时 ==> {} ms", orderDTOS.size(), System.currentTimeMillis() - time);
    }

    @Override
    public void run(String... args) throws Exception {
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
