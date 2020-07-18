package com.lyf.scm.job.task;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.lyf.scm.job.api.dto.MailSendInfoDTO;
import com.lyf.scm.job.config.AbstractSimpleJob;
import com.lyf.scm.job.remote.facade.MailSendFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 邮件发送Job <br>
 *
 * @Author chuwenchao 2020/4/9
 */
@Slf4j
@Component
public class MailSendJob extends AbstractSimpleJob {

    @Value("${elastic.job.mailSendJob.jobName}")
    private String jobName;
    @Value("${elastic.job.mailSendJob.cron}")
    private String cron;
    @Value("${elastic.job.mailSendJob.shardingTotalCount}")
    private Integer shardingTotalCount;
    @Value("${elastic.job.mailSendJob.shardingItemParameters}")
    private String shardingItemParameters;
    @Value("${elastic.job.mailSendJob.failover}")
    private Boolean failover;
    @Value("${elastic.job.mailSendJob.desc}")
    private String desc;

    @Resource
    private MailSendFacade mailSendFacade;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("邮件发送Job启动。。。");
        Long time = System.currentTimeMillis();
        List<MailSendInfoDTO> messageDTOS = mailSendFacade.queryMailInfoIntervalSeven();
        if(CollectionUtils.isEmpty(messageDTOS)) {
            return ;
        }
        for (MailSendInfoDTO dto : messageDTOS) {
            try {
                mailSendFacade.sendMailJob(dto);
            } catch (Exception e) {
                log.info("邮件发送异常，参数 ==> {}", JSONObject.toJSONString(dto));
            }
        }
        time = System.currentTimeMillis() - time;
        log.info("邮件发送Job启动执行成功，执行条数 ==> {}, 耗时 ==> {} ms", messageDTOS.size(), time);
    }

    @Override
    public void run(String... args){
        // spring 启动完成后初始化job
        registerSimpleJob(jobName, cron, shardingTotalCount, shardingItemParameters, failover, desc);
    }
}
