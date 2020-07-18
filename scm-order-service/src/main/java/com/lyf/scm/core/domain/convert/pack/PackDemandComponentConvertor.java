package com.lyf.scm.core.domain.convert.pack;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.pack.PackDemandComponentDTO;
import com.lyf.scm.core.domain.entity.pack.PackDemandComponentE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 包装需求单明细原料对象转换器
 * <p>
 * @Author: wwh 2020/7/7
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface PackDemandComponentConvertor {

	PackDemandComponentDTO convertE2DTO(PackDemandComponentE packDemandComponentE);
	
	PackDemandComponentE convertDTO2E(PackDemandComponentDTO packDemandComponentDTO);
	
    List<PackDemandComponentDTO> convertEList2DTOList(List<PackDemandComponentE> packDemandComponentEList);
    
    List<PackDemandComponentE> convertDTOList2EList(List<PackDemandComponentDTO> packDemandComponentDTOList);
    
}