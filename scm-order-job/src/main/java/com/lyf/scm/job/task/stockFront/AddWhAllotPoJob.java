package com.lyf.scm.job.task.stockFront;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.common.constants.PageConstants;
import com.lyf.scm.job.api.dto.stockFront.WhAllocationDTO;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.stockFront.facade.WhAllocationFacade;

import lombok.extern.slf4j.Slf4j;

/**
 * 类AddWhAllotPoJob的实现描述：推送调拨PO
 *
 * @author sunyj 2019/7/1 20:03
 */
@Slf4j
@Component
public class AddWhAllotPoJob  extends AbstractSimpleJob {
    @Value("${elastic.job.addWhAllotPoJob.jobName}")
    private String jobName;
    @Value("${elastic.job.addWhAllotPoJob.cron}")
    private String cron;
    @Value("${elastic.job.addWhAllotPoJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.addWhAllotPoJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.addWhAllotPoJob.failover}")
    private Boolean failover;
    @Resource
    private WhAllocationFacade whAllocationFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("调拨同公司采购订单下发到SAP系统Job启动。。。");
        Long time = System.currentTimeMillis();
        // 查询结果
        List<WhAllocationDTO> recordList = null;
        do {
            recordList = whAllocationFacade.getWaitSyncOrder(0, PageConstants.ROWS_200);
            if(CollectionUtils.isEmpty(recordList)) {
                break;
            }
            recordList.forEach(record -> {
                try {
                    log.error("开始处理调拨单--"+ record.getRecordCode());
                    whAllocationFacade.processWhAllocationOrderToSap(record);
                } catch (Exception e) {
                    log.error("调拨同公司采购订单【{}】下发到SAP系统异常", record.getRecordCode(), e);
                }
            });
        } while(!CollectionUtils.isEmpty(recordList) && recordList.size() == PageConstants.ROWS_200);

        time = System.currentTimeMillis() - time;
        log.info("调拨同公司采购订单下发到SAP系统Job执行成功，耗时 ==> {} ms", time);
    }

    @Override
    public void run(String... args) throws Exception {
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, "推送调拨PO给SAP");
    }
}
