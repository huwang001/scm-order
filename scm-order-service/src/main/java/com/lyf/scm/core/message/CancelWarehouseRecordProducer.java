package com.lyf.scm.core.message;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.common.constants.KibanaLogConstants;
import com.lyf.scm.core.config.ServiceKibanaLog;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 回滚 - 出入库单 异步消息 发送
 */
@Slf4j
@Service
public class CancelWarehouseRecordProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.shelf.cancelWarehouseRecord.topic}")
    private String topic;
    @Value("${rocketmq.timeOut}")
    private Long timeOut;

    public void sendAsyncMQ(CancelRecordDTO cancelRecord) {
        String msg = JSON.toJSONString(cancelRecord);
        log.info("发送-取消出入库单消息：" + msg);
        long start = System.currentTimeMillis();
        //mq信息
        Message message = MessageBuilder.withPayload(msg).build();
        rocketMQTemplate.asyncSend(topic, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                long endSucc = System.currentTimeMillis();
                log.info("发送-取消出入库单消息-成功-异步消息，后置单号：{}，发送时间：{} 毫秒", cancelRecord.getRecordCode(), (endSucc - start));
            }

            @Override
            public void onException(Throwable throwable) {
                String msg = String.format("取消出入库单-发送消息失败-异步消息，后置单据号：%s， 请求参数：%s", cancelRecord.getRecordCode(), JSON.toJSONString(cancelRecord));
                log.warn(ServiceKibanaLog.getServiceLog(KibanaLogConstants.ROLL_BACK_EXCEPTION, "cancelWarehouseRecord", msg, cancelRecord));
            }
        }, timeOut);
    }
}
