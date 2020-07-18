package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdjustForetasteMapper {

    Integer insert(AdjustForetasteE record);

    AdjustForetasteE selectByPrimaryKey(Long id);

    List<AdjustForetasteE> selectShopAdjustForetasteList(ShopAdjustRecordDTO param);

    /**
     * 门店试吃单幂等校验
     *
     * @param outRecordCode
     * @param shopCode
     * @return
     */
    Integer selectCountByOutRecordCode(@Param("outRecordCode") String outRecordCode, @Param("shopCode") String shopCode);
}