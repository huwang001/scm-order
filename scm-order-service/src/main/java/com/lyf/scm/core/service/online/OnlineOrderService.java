package com.lyf.scm.core.service.online;

import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;

public interface OnlineOrderService {

    /**
     * 电商下单锁库存
     *
     * @param stockOrderDTO
     */
    void lockStockByRecord(OnlineOrderDTO stockOrderDTO);
}
