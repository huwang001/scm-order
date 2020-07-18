package com.lyf.scm.core.domain.convert.stockFront;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.stockFront.BusinessReasonDTO;
import com.lyf.scm.core.domain.entity.stockFront.BusinessReasonE;
import com.rome.arch.core.domain.EntityFactory;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface BusinessReasonConvertor {

	BusinessReasonDTO convertE2DTO(BusinessReasonE businessReasonE);

	List<BusinessReasonDTO> convertEList2DTOList(List<BusinessReasonE> businessReasonEList);

}