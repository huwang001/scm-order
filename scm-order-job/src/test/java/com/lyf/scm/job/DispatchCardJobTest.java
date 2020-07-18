package com.lyf.scm.job;

import com.lyf.scm.job.remote.stockFront.facade.WarehouseRecordServiceFacade;
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
public class DispatchCardJobTest {

    @Resource
    private WarehouseRecordServiceFacade warehouseRecordServiceFacade;

    @Test
    public void handle() {
        System.out.println("批量同步派车系统出库单Job启动。。。");
        Long time = System.currentTimeMillis();
        List<Long> warehouseRecordList = warehouseRecordServiceFacade.queryNeedSyncTmsBWarehouseRecords(0, 10);
        System.out.println("查询未同步的出库单：" + warehouseRecordList.toString());
        warehouseRecordList.forEach(id -> {
            try {
                warehouseRecordServiceFacade.handleDispatchCarWarehouseRecord(id);
            } catch (Exception e) {
                System.out.println("同步派车系统处理失败" + "盘点单id{" + id + "}:" + e.getMessage());
            }
        });
        time = System.currentTimeMillis() - time;
        System.out.println("批量同步派车系统出库单Job成功，耗时 ==> { " + time + "} ms");
    }
}
