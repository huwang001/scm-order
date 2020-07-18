package com.lyf.scm.core.service.online;

import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.domain.entity.online.SaleE;

public interface SaleService {

    /**
     * 判断单据是否已经存在
     *
     * @param outRecordCode
     * @return
     */
    Boolean judgeExistByOutRecordCode(String outRecordCode);

    /**
     * 保存前置单信息
     *
     * @param onlineOrder
     */
    SaleE addSaleFrontRecord(OnlineOrderDTO onlineOrder);
}
