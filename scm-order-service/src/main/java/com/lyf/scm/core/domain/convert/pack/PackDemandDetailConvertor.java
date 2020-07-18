package com.lyf.scm.core.domain.convert.pack;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.pack.PackDemandDetailDTO;
import com.lyf.scm.core.domain.entity.pack.PackDemandDetailE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 包装需求单明细对象转换器
 * <p>
 * @Author: wwh 2020/7/7
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface PackDemandDetailConvertor {

	PackDemandDetailDTO convertE2DTO(PackDemandDetailE packDemandDetailE);
	
	PackDemandDetailE convertDTO2E(PackDemandDetailDTO packDemandDetailDTO);
	
    List<PackDemandDetailDTO> convertEList2DTOList(List<PackDemandDetailE> packDemandDetailEList);
    
    List<PackDemandDetailE> convertDTOList2EList(List<PackDemandDetailDTO> packDemandDetailDTOList);
    
}