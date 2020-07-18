//package com.lyf.scm.core.message;
//
//import com.alibaba.fastjson.JSONObject;
//import com.lyf.scm.core.service.common.RealWarehouseAllocationService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.Resource;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
///**
// * 接收库存团购发货回调
// */
//@Slf4j
//@Service
//@RocketMQMessageListener(topic = "${rocketmq.consumer.groupPurchaseOutNotify.topic}", selectorExpression = "groupPurchaseOutNotify", consumerGroup = "${rocketmq.consumer.groupPurchaseOutNotify.group}")
//public class GroupPurchaseOutNotifyConsumer implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {
//
//    @Resource
//    private RealWarehouseAllocationService realWarehouseAllocationService;
//    @Override
//    public void onMessage(MessageExt messageExt) {
//        log.info("接收库存团购发货回调消息消费，msgID: {}，消费次数: {}", messageExt.getMsgId(), messageExt.getReconsumeTimes());
////        String outRecordCode = JSONObject.parseObject(, String.class);
//        String outRecordCode = null ;
//        try {
//            outRecordCode = new String(messageExt.getBody(),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
////        log.warn(CoreKibanaLog.getJobLog(KibanaLogConstants.CONSUMER_MQ, "addShopConsumer", messageExt.getMsgId(), storeDTOs));
//        if(StringUtils.isEmpty(outRecordCode)) {
//            log.error("接收库存团购发货回调消息消费内容为空，msgID: {}", messageExt.getMsgId());
//            return ;
//        }
//        realWarehouseAllocationService.deliverOutNotifyToGroupPurchase(outRecordCode);
//    }
//
//    @Override
//    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
//        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//    }
//}
