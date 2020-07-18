package com.lyf.scm.core.domain.convert.online;

import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.api.dto.online.OnlineOrderDetailDTO;
import com.lyf.scm.core.domain.entity.online.SaleE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: APP-电商销售转换
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface SaleConvertor {

    @Mappings(
            @Mapping(source = "orderCode", target = "outRecordCode")
    )
    SaleE orderDtoToSaleEntity(OnlineOrderDTO onlineOrderDTO);
}