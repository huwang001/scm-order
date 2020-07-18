package com.lyf.scm.core.service.order.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lyf.scm.common.enums.FrontRecordTypeEnum;
import com.lyf.scm.common.util.Snowflake;
import com.lyf.scm.core.config.RedisUtil;
import com.lyf.scm.core.domain.entity.order.OrderSequenceE;
import com.lyf.scm.core.mapper.order.OrderSequenceMapper;
import com.lyf.scm.core.service.order.OrderUtilService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service("orderUtilService")
public class OrderUtilServiceImpl implements OrderUtilService {

    /**
     * 当日日期
     */
    private final static String ORDER_CODE_LOCK_REDIS_KEY = "orderCode-date";

    /**
     * 订单序列
     */
    private final static String ORDER_SEQUENCE_REDIS_KEY = "order-sequence";

    /**
     *	自动释放的时间
     */
    private final static Integer REDIS_LAST_TIME = 60 * 60 * 24 * 1;

    //门店调拨的单号最长16位，数字15位
    private final static int SOCODELEN = 15;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private OrderSequenceMapper orderSequenceMapper;
    @Value("${sequence.step:10000}")
    private Long sequenceStep;

    /**
     * @Description: 获取订单序列号 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param prefix
     * @return
     */
    @Override
    public String queryOrderCodeBySnow(String prefix) {
        return prefix + Snowflake.getInstanceSnowflake().nextId();
    }

    @Override
    public String queryOrderCodeByDate(String prefix) {
        try {
            long time = System.currentTimeMillis();
            Long sequence = this.queryOrderSequence();
            log.info("序列号 == > {},耗时 ==> {}", sequence, System.currentTimeMillis() - time);
            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
            StringBuffer sb = new StringBuffer();
            sb.append(prefix).append(format.format(new Date())).append(String.format("%07d", sequence));
            return sb.toString();
        } catch (Exception e) {
            log.info("获取日期订单号异常", e);
            if(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getCode().equals(prefix)) {
                return getShopAllocationCode(prefix);
            } else {
                return this.queryOrderCodeBySnow(prefix);
            }
        }
    }

    /**
     * @Description: 获取订单号（带日期） <br>
     *
     * @Author chuwenchao 2020/7/2
     * @param prefix
     * @return
     */
    @Override
    public String queryOrderCode(String prefix) {
        if(FrontRecordTypeEnum.SHOP_ALLOCATION_RECORD.getCode().equals(prefix)) {
            return getShopAllocationCode(prefix);
        } else {
            return this.queryOrderCodeBySnow(prefix);
        }
    }

    private String getShopAllocationCode(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String soCode = sdf.format(new Date());
        int idLen = StringUtils.length(soCode);
        int left = SOCODELEN - 6;
        if (idLen < left) {
            for (int i = 0; i < left - idLen; i++) {
                soCode = "0".concat(soCode);
            }
        }
        soCode = prefix + soCode;
        //增加100-1000内的随机数
        Random rd=new Random();
        int ram = rd.nextInt(900) + 100;
        soCode = soCode  + String.valueOf(ram);
        return soCode;
    }

    /**
     * @Description: 获取单号序列<br>
     *
     * @Author chuwenchao 2020/7/2
     * @return
     */
    private synchronized Long queryOrderSequence() {
        //查询Redis
        String redisData = (String) redisUtil.get(ORDER_SEQUENCE_REDIS_KEY);
        OrderSequenceE orderSequenceE = null;
        if(StringUtils.isNotBlank(redisData)) {
            orderSequenceE = JSONObject.parseObject(redisData, new TypeReference<OrderSequenceE>(){});
        }
        Date currentDate = new Date();
        if(orderSequenceE == null) {
            //查库
            orderSequenceE = orderSequenceMapper.getOrderSequence();
            if(!DateUtil.isSameDay(orderSequenceE.getUpdateTime(), currentDate)) {
                //设置初始值
                orderSequenceE.setCurrentSequence(Math.round(Math.random() * 10) + 1);
                orderSequenceE.setSectionNum(sequenceStep);
            } else {
                //对初始值累加一个步长
                orderSequenceE.setCurrentSequence(orderSequenceE.getSectionNum() + Math.round(Math.random() * 10) + 1);
                orderSequenceE.setSectionNum(orderSequenceE.getSectionNum() + sequenceStep);
            }
            orderSequenceE.setCurrentDate(currentDate);
            //更新数据库
            orderSequenceMapper.updateOrderSequenceId(orderSequenceE);
            orderSequenceE.setVersion(orderSequenceE.getVersion()+1);
            redisUtil.set(ORDER_SEQUENCE_REDIS_KEY, JSONObject.toJSONString(orderSequenceE), REDIS_LAST_TIME);
            return orderSequenceE.getCurrentSequence();
        }
        //比较日期是否变化
        boolean isSameDay = DateUtil.isSameDay(orderSequenceE.getCurrentDate(), currentDate);
//        log.info("缓存日期【{}】与当前日期【{}】是否一致【{}】",orderSequenceE.getCurrentDate(), currentDate, isSameDay);
        if(!isSameDay) {
            //设置初始值
            orderSequenceE.setCurrentSequence(Math.round(Math.random() * 10) + 1);
            orderSequenceE.setSectionNum(sequenceStep);
            orderSequenceE.setCurrentDate(currentDate);
            //更新数据库
            orderSequenceMapper.updateOrderSequenceId(orderSequenceE);
            orderSequenceE.setVersion(orderSequenceE.getVersion()+1);
            redisUtil.set(ORDER_SEQUENCE_REDIS_KEY, JSONObject.toJSONString(orderSequenceE), REDIS_LAST_TIME);
            return orderSequenceE.getCurrentSequence();
        }
        //生成序列
        Long currentSequence = orderSequenceE.getCurrentSequence() + 1;
        if(currentSequence.compareTo(orderSequenceE.getSectionNum()) > 0) {
            //对初始值累加一个步长
            orderSequenceE.setCurrentSequence(orderSequenceE.getSectionNum()+ Math.round(Math.random() * 10) +1);
            orderSequenceE.setSectionNum(orderSequenceE.getSectionNum() + sequenceStep);
            orderSequenceE.setCurrentDate(currentDate);
            //更新数据库
            orderSequenceMapper.updateOrderSequenceId(orderSequenceE);
            orderSequenceE.setVersion(orderSequenceE.getVersion()+1);
            redisUtil.set(ORDER_SEQUENCE_REDIS_KEY, JSONObject.toJSONString(orderSequenceE), REDIS_LAST_TIME);
            return orderSequenceE.getCurrentSequence();
        } else {
            //存入Redis
            orderSequenceE.setCurrentSequence(currentSequence);
            redisUtil.set(ORDER_SEQUENCE_REDIS_KEY, JSONObject.toJSONString(orderSequenceE), REDIS_LAST_TIME);
            return currentSequence;
        }
    }

}
