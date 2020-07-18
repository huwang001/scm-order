package com.lyf.scm.core.domain.convert.order;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.order.OrderDTO;
import com.lyf.scm.core.api.dto.order.OrderDetailRespDTO;
import com.lyf.scm.core.domain.entity.order.OrderDetailE;
import com.lyf.scm.core.domain.entity.order.OrderE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 预约单明细对象转换器 <br>
 *
 * @Author chuwenchao 2020/4/10
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface OrderDetailConvert {

	List<OrderDetailRespDTO> convertEList2DTOList(List<OrderDetailE> orderDetailEList);
	
	List<OrderDTO> convertEList2DTOList2(List<OrderE> orderEList);
	
}