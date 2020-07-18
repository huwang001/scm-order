package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteDetailE;
import com.lyf.scm.core.domain.model.stockFront.AdjustForetasteDetailDO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 试吃调整单 明细
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface AdjustForetasteDetailConvertor {

    AdjustForetasteDetailDO convertE2DTO(AdjustForetasteDetailE detailE);

    List<AdjustForetasteDetailDO> convertE2DTOList(List<AdjustForetasteDetailE> adjustForetastes);
}