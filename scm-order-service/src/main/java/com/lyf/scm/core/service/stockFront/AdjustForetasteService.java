package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;

public interface AdjustForetasteService {

    /**
     * 门店 试吃 调整单 创建
     *
     * @author zhanglong
     * @date 2020/7/14 13:58
     */
    void addShopForetasteRecord(ShopAdjustRecordDTO frontRecord);

    /**
     * 分页查询 试吃 调整单 列表
     *
     * @author zhanglong
     * @date 2020/7/14 13:58
     */
    PageInfo<ShopAdjustRecordDTO> findShopForetasteCondition(ShopAdjustRecordDTO param, int pageNum, int pageSize);
}
