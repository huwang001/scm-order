package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

public interface AdjustForetasteToWareHouseRecordService {

    /**
     * 根据试吃调整单 创建出库单
     *
     * @author zhanglong
     * @date 2020/7/14 13:58
     */
    WarehouseRecordE createWarehouseRecordByAdjustForetaste(AdjustForetasteE frontRecord);
}
