package com.lyf.scm.core.domain.convert.orderReturn;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDetailDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDetailDTO;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnDetailE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 退货单详情对象转换器
 * <p>
 * @Author: wwh 2020/4/8
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface OrderReturnDetailConvert {
	
	OrderReturnDetailDTO convertE2DTO(OrderReturnDetailE orderReturnDetailE);
	
	OrderReturnDetailE convertDTO2E(OrderReturnDetailDTO orderReturnDetailDTO);
	
    List<OrderReturnDetailDTO> convertEList2DTOList(List<OrderReturnDetailE> orderReturnDetailEList);
    
    List<OrderReturnDetailE> convertDTOList2EList(List<OrderReturnDetailDTO> orderReturnDetailDTOList);
    
    List<OrderReturnDetailE> convertDTOList2EList2(List<ReturnDetailDTO> returnDetailDTOList);

}