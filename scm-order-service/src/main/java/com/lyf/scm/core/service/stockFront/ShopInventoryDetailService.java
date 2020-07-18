package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;

/**
 * 门店盘点 明细
 */
public interface ShopInventoryDetailService {

    /**
     * 保存门店盘点明细
     *
     * @param frontRecordE
     * @return
     * @author zhanglong
     */
    boolean saveShopInventoryDetail(ShopInventoryE frontRecordE);
}
