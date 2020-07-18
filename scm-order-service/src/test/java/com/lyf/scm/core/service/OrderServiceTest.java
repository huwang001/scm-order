package com.lyf.scm.core.service;

import com.lyf.scm.core.remote.base.facade.BaseFacade;
import com.lyf.scm.core.service.order.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Resource
    private OrderService orderService;
    @Resource
    private BaseFacade baseFacade;

    @Test
    public void notifyTradeAfterLocked() {
        baseFacade.searchByCode("107U");
    }
}