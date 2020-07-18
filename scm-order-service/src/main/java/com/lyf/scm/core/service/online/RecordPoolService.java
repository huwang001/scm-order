package com.lyf.scm.core.service.online;

import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import com.lyf.scm.core.domain.entity.online.SaleE;
import com.lyf.scm.core.remote.stock.dto.CoreVirtualStockOpDO;

import java.util.List;

public interface RecordPoolService {

    /**
     * 保存子do单和明细
     *
     * @param onlineRetailE
     * @return
     */
    RecordPoolE saveRecordPool(SaleE onlineRetailE, int needCombine, Long realWarehouseId, Long virtualWarehouseId, List<CoreVirtualStockOpDO> cvsList);
}
