package com.lyf.scm.core.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;
import com.lyf.scm.core.remote.stock.dto.CancelResultDTO;
import com.lyf.scm.core.remote.stock.facade.StockRecordFacade;
import com.rome.arch.core.exception.RomeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 回滚 - 出入库单 消费
 */
@Slf4j
@Service
@RocketMQMessageListener(topic = "${rocketmq.shelf.cancelWarehouseRecord.topic}", consumerGroup = "${rocketmq.shelf.cancelWarehouseRecord.group}")
public class CancelWarehouseRecordConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    @Resource
    private StockRecordFacade stockRecordFacade;

    @Override
    public void onMessage(MessageExt msg) {
        log.info("接收-取消出入库单消息，msgId:{}，消费次数，times:{}", msg.getMsgId(), msg.getReconsumeTimes());
        CancelRecordDTO cancelRecord = JSONObject.parseObject(msg.getBody(), CancelRecordDTO.class);
        log.info("接收-取消出入库单消息，消息内容: {}", JSON.toJSONString(cancelRecord));
        CancelResultDTO result = stockRecordFacade.cancelSingleRecord(cancelRecord);
        if (null == result || !result.getStatus()) {
            throw new RomeException(ResCode.ORDER_ERROR_9902, ResCode.ORDER_ERROR_9902_DESC);
        }
        log.info("取消出入库单成功，请求参数：{}，返回结果：{}", JSON.toJSONString(cancelRecord), JSON.toJSONString(result));
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        /**
         * 一个新的订阅组第一次启动从指定时间点开始消费<br>
         * 后续再启动接着上次消费的进度开始消费 时间点设置参见DefaultMQPushConsumer.consumeTimestamp参数<br>
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        //消费时间戳
        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
    }
}
