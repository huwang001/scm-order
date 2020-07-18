package com.lyf.scm.core.domain.convert.orderReturn;

import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDTO;
import com.lyf.scm.core.api.dto.orderReturn.ReturnDTO;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnE;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 退货单对象转换器
 * <p>
 * @Author: wwh 2020/4/8
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface OrderReturnConvert {
	
	OrderReturnDTO convertE2DTO(OrderReturnE orderReturnE);
	
	OrderReturnE convertDTO2E(OrderReturnDTO orderReturnDTO);
	
	OrderReturnE convertDTO2E(ReturnDTO returnDTO);
	
    List<OrderReturnDTO> convertEList2DTOList(List<OrderReturnE> orderReturnEList);
    
    List<OrderReturnE> convertDTOList2EList(List<OrderReturnDTO> orderReturnDTOList);

	List<FrontRecordE> convertEListToEList(List<OrderReturnE> orderReturnEList);

}