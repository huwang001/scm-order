package com.lyf.scm.core.service.stockFront.impl;

import com.lyf.scm.core.domain.convert.stockFront.ReplenishRecordConvert;
import com.lyf.scm.core.service.stockFront.ColdChainOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 冷链服务ServiceImpl
 * <p>
 * @Author: chuwenchao  2020/6/13
 */
@Slf4j
@Service("coldChainOrderService")
public class ColdChainOrderServiceImpl implements ColdChainOrderService {

    @Resource
    private ReplenishRecordConvert replenishRecordConvert;


}
