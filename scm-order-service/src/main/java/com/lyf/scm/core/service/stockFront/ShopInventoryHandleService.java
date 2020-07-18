package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;

public interface ShopInventoryHandleService {

    /**
     * 全盘
     *
     * @param frontRecord
     * @throws Exception
     * @author zhanglong
     */
    public void allInventory(ShopInventoryE frontRecord) throws Exception;

    /**
     * 账面库存盘点
     *
     * @param frontRecord
     * @throws Exception
     * @author zhanglong
     */
    public void accountInventory(ShopInventoryE frontRecord) throws Exception;
}
