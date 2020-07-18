package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopSaleDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.ShopSaleDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店零售单明细
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopSaleDetailConvertor {

    ShopSaleDetailE shopSaleDetailDto2Entity(ShopSaleDetailDTO saleDetailDTO);

    List<ShopSaleDetailE> shopSaleListDetailDto2EntityList(List<ShopSaleDetailDTO> saleDetailDTOList);
}