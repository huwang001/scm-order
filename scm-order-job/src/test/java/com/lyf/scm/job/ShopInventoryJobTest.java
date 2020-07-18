package com.lyf.scm.job;

import com.lyf.scm.job.remote.stockFront.facade.ShopInventoryRecordServiceFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev-zt")
public class ShopInventoryJobTest {

    @Autowired
    private ShopInventoryRecordServiceFacade shopInventoryRecordServiceFacade;

    @Test
    public void handle() {
        System.out.println("批量处理门店盘点单Job启动。。。");
        Long time = System.currentTimeMillis();
        List<Long> inventoryIds = shopInventoryRecordServiceFacade.queryInitShopInventoryRecords(0, 10);
        System.out.println("查询出来的未处理盘点单：" + inventoryIds.toString());
        inventoryIds.forEach(id -> {
            try {
                shopInventoryRecordServiceFacade.handleShopInventoryRecords(id);
            } catch (Exception e) {
                System.out.println("盘点单处理失败" + "盘点单id{" + id + "}:" + e.getMessage());
            }
        });
        time = System.currentTimeMillis() - time;
        System.out.println("批量处理门店盘点单Job执行成功，耗时 ==> { " + time + "} ms");
    }
}
