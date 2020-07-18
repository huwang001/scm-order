package com.lyf.scm.core.domain.convert.reverse;

import java.util.List;

import com.lyf.scm.core.api.dto.reverse.ReverseInfoDTO;
import com.lyf.scm.core.api.dto.reverse.ReverseReponseDTO;
import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.reverse.ReverseDTO;
import com.lyf.scm.core.domain.entity.reverse.ReverseE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 冲销单对象转换器
 * <p>
 * @Author: wwh 2020/7/16
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ReverseConvert {
	
	ReverseDTO convertE2DTO(ReverseE reverseE);
	
	ReverseE convertDTO2E(ReverseDTO reverseDTO);
	
    List<ReverseDTO> convertEList2DTOList(List<ReverseE> reverseEList);
    
    List<ReverseE> convertDTOList2EList(List<ReverseDTO> reverseDTOList);

    ReverseE convertInfoDTO2E(ReverseInfoDTO reverseInfoDTO);

    List<ReverseReponseDTO> convertEListToDtoList(List<ReverseE> reverseEList);

}