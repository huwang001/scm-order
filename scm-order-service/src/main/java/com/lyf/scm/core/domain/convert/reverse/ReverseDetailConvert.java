package com.lyf.scm.core.domain.convert.reverse;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.reverse.ReverseDetailDTO;
import com.lyf.scm.core.domain.entity.reverse.ReverseDetailE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 冲销单明细对象转换器
 * <p>
 * @Author: wwh 2020/7/16
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ReverseDetailConvert {
	
	ReverseDetailDTO convertE2DTO(ReverseDetailE reverseDetailE);
	
	ReverseDetailE convertDTO2E(ReverseDetailDTO reverseDetailDTO);
	
    List<ReverseDetailDTO> convertEList2DTOList(List<ReverseDetailE> reverseDetailEList);
    
    List<ReverseDetailE> convertDTOList2EList(List<ReverseDetailDTO> reverseDetailDTOList);

}