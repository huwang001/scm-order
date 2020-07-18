package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;

public interface AdjustForetasteDetailService {

    /**
     * 查询试吃 调整单 明细
     *
     * @author zhanglong
     * @date 2020/7/14 13:57
     */
    ShopAdjustRecordDTO getAdjustForetasteByRecordId(Long recordId);

    /**
     * 保存 试吃调整单 明细
     *
     * @author zhanglong
     * @date 2020/7/14 13:56
     */
    boolean saveAdjustForetasteDetail(AdjustForetasteE frontRecord);
}
