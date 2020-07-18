package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopAdjustRecordDTO;
import com.lyf.scm.core.domain.entity.stockFront.AdjustForetasteE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店试吃调整单
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface AdjustForetasteConvertor {

    ShopAdjustRecordDTO convertE2DTO(AdjustForetasteE adjustForetaste);

    AdjustForetasteE dtoToEntity(ShopAdjustRecordDTO dto);

    List<ShopAdjustRecordDTO> convertE2DTOList(List<AdjustForetasteE> adjustForetastes);
}