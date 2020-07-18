package com.lyf.scm.core.domain.convert.order;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.order.OrderRespDTO;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 预约单model转换器 <br>
 *
 * @Author chuwenchao 2020/4/10
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface OrderConvert {
	
	OrderRespDTO convertE2DTO(OrderE orderE);

	List<OrderRespDTO> convertEList2DTOList(List<OrderE> orderEList);
    
}
