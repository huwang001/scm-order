package com.lyf.scm.core.service;

import com.lyf.scm.core.service.order.OrderUtilService;
import com.lyf.scm.core.service.stockFront.ShopReplenishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev-zt")
public class ReplenishServiceTest {

    @Resource
    private ShopReplenishService shopReplenishService;
    @Resource
    private OrderUtilService orderUtilService;

    @Test
    public void dispatchResultReplenishComplete() throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++) {
                    System.out.println(orderUtilService.queryOrderCode("S"));
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++) {
                    System.out.println(orderUtilService.queryOrderCode("S"));
                }
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++) {
                    System.out.println(orderUtilService.queryOrderCode("S"));
                }
            }
        });
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++) {
                    System.out.println(orderUtilService.queryOrderCode("S"));
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        Thread.sleep(100000L);
    }
}
