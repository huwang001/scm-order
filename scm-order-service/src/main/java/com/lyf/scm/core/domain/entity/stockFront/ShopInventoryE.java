package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.ShopInventoryDO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ShopInventoryE extends ShopInventoryDO {

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 盘点详情
     */
    List<ShopInventoryDetailE> frontRecordDetails;
}
