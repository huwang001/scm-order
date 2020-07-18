package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.InventoryDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店盘点调整单
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopInventoryDetailConvertor {

    ShopInventoryDetailE dtoToEntity(InventoryDetailDTO detailDTO);

    List<ShopInventoryDetailE> dtoListToEntityList(List<InventoryDetailDTO> detailDTOList);
}