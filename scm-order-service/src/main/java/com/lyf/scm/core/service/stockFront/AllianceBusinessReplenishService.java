package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.FrontWarehouseRecordRelationE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

import java.util.List;

/**
 * 类AllianceBusinessReplenishService的实现描述：加盟商叫货
 *
 * @author sunyj 2019/6/6 11:55
 */
public interface AllianceBusinessReplenishService {


    /**
     * 确认销售出库
     * @param frontWarehouseRecordRelationE
     *
     */
    void warehouseOutNotify(FrontWarehouseRecordRelationE frontWarehouseRecordRelationE);

    /**
     * 寻源保存出入库单
     * @param detail
     * @param realWarehouse
     * @param vmCode
     */
     void saveAllotWarehouseRecord(List<ReplenishDetailE> detail, RealWarehouse realWarehouse, String vmCode);
}
