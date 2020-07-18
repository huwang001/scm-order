package com.lyf.scm.core.domain.convert.online;

import com.lyf.scm.core.api.dto.online.OnlineOrderDTO;
import com.lyf.scm.core.domain.entity.online.AddressE;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

/**
 * @Description: 地址-转换
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface AddressConvertor {

    AddressE orderDtoToAddressEntity(OnlineOrderDTO onlineOrderDTO);

    AddressE orderReturnEToAddressE(OrderReturnE orderReturnE);
}