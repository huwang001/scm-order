package com.lyf.scm.core.message;

import javax.annotation.Resource;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.core.config.ServiceKibanaLog;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 包装需求单领料状态异步消息发送
 * <p>
 * @Author: wwh 2020/7/7
 */
@Slf4j
@Service
public class PackDemandPickStatusProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.producer.packDemandPickStatus.topic}")
    private String topic;
    @Value("${rocketmq.timeOut}")
    private Long timeOut;

    public void sendAsyncMQ(JSONObject jsonObject) {
        String msg = JSON.toJSONString(jsonObject);
        log.info("发送-包装需求单领料状态消息：" + msg);
        long startTime = System.currentTimeMillis();
        Message message = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                long endTime = System.currentTimeMillis();
                log.info("发送-包装需求单领料状态消息-成功-异步消息，需求单号：{}，发送时间：{} 毫秒", jsonObject.getString("requireCode"), (endTime - startTime));
            }

            @Override
            public void onException(Throwable throwable) {
                String msg = String.format("包装需求单领料状态-发送消息失败-异步消息，需求单号：%s， 请求参数：%s", jsonObject.getString("requireCode"), JSON.toJSONString(jsonObject));
                log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.PACK_DEMAND_PICK_STATUS, "packDemandPickStatus", msg, jsonObject));
            }
        }, timeOut);
    }
    
}