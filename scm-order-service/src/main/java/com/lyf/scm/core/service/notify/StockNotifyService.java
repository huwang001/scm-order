package com.lyf.scm.core.service.notify;

import com.lyf.scm.core.api.dto.notify.StockNotifyDTO;

/**
 * @Description: 库存回调通知Service
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
public interface StockNotifyService {

    /**
     * @Description: 库存出库结果通知 <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param stockNotifyDTO
     * @return 
     */
    void outRecordNotify(StockNotifyDTO stockNotifyDTO);

    /**
     * @Description: 库存入库结果通知 <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param stockNotifyDTO
     * @return
     */
    void inRecordNotify(StockNotifyDTO stockNotifyDTO);
}
