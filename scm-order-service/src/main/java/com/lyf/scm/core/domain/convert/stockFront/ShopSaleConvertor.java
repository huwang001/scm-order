package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopSaleRecordDTO;
import com.lyf.scm.core.domain.entity.stockFront.ShopSaleE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

/**
 * @Description: 门店零售单
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopSaleConvertor {

    ShopSaleE shopSaleDto2Entity(ShopSaleRecordDTO saleRecordDTO);

}