package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdjustForetasteDetailMapper {

    List<AdjustForetasteDetailE> queryAdjustForetasteDetailByRecordId(Long recordId);

    Integer saveFrAdjustForetasteDetailList(@Param("frontRecordDetails") List<AdjustForetasteDetailE> detailEList);
}