package com.lyf.scm.job;

import com.alibaba.fastjson.JSON;
import com.lyf.scm.job.remote.shopReturn.facade.ShopReturnRecordServiceFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev-zt")
public class PushShopReturnToTradeJobTest {

    @Resource
    private ShopReturnRecordServiceFacade shopReturnRecordServiceFacade;

    @Test
    public void handle() {
        System.out.println("门店退货单-同步交易中心Job启动。。。");
        Long time = System.currentTimeMillis();
        List<String> frontRecordList = shopReturnRecordServiceFacade.queryUnPushTradeShopReturnRecord(0, 200);
        System.out.println("门店退货单-待推送单据：" + JSON.toJSONString(frontRecordList));
        frontRecordList.forEach(recordCode -> {
            try {
                shopReturnRecordServiceFacade.pushTradeShopReturnRecord(recordCode);
            } catch (Exception e) {
                System.out.println("门店退货单-同步交易中心异常：" + recordCode);
            }
        });
        time = System.currentTimeMillis() - time;
        System.out.println("门店退货单-同步交易中心Job成功，耗时 ==> { " + time + "} ms");
    }
}
