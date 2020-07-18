package com.lyf.scm.core.service.notify;

import com.lyf.scm.core.api.dto.notify.TmsNotifyDTO;

/**
 * @Description: 库存回调通知Service
 * <p>
 * @Author: huwang  2020/6/19
 */
public interface TmsNotifyService {

    /**
     * 创建派车通知
     * @param tmsNotifyDTO
     */
   void dispatchTMSNotify(TmsNotifyDTO tmsNotifyDTO);

    /**
     * 修改派车通知
     * @param tmsNotifyDTO
     */
   void updateDispatchNotify(TmsNotifyDTO tmsNotifyDTO);
}
