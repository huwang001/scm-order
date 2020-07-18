package com.lyf.scm.core.service.reverse;

import com.lyf.scm.core.domain.entity.pack.PackTaskFinishDetailE;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;
import com.lyf.scm.core.domain.entity.reverse.ReverseE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

import java.util.List;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/17
 */
public interface ReverseOperationToWhRecordService {

    /**
     *出入库冲销  生成出库单
     *
     * @param reverseE
     * @return
     */
    WarehouseRecordE createOutWhRecordByTaskOperation(ReverseE reverseE);

    /**
     * 出入库冲销 生成入库单
     * @param reverseE
     * @return
     */
    WarehouseRecordE createInWhRecordByTaskOperation(ReverseE reverseE);
}
