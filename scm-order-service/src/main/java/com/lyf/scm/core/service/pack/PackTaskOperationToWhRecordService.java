package com.lyf.scm.core.service.pack;

import com.lyf.scm.core.domain.entity.pack.PackTaskFinishDetailE;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

import java.util.List;

public interface PackTaskOperationToWhRecordService {

    /**
     * 包装任务完成清单 生成出库单
     *
     * @param finishE
     * @param finishDetailList
     * @return
     */
    WarehouseRecordE createOutWhRecordByTaskOperation(PackTaskFinishE finishE, List<PackTaskFinishDetailE> finishDetailList);

    /**
     * 包装任务完成清单 生成入库单
     *
     * @param finishE
     * @param finishDetailList
     * @return
     */
    WarehouseRecordE createInWhRecordByTaskOperation(PackTaskFinishE finishE, List<PackTaskFinishDetailE> finishDetailList);
}
