package com.lyf.scm.core.domain.convert.stockFront;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.stockFront.CustomizeTableRowDTO;
import com.lyf.scm.core.domain.entity.stockFront.CustomizeTableRowE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 自定义对象转换器
 *               <p>
 * @Author: wwh 2020/6/15
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface CustomizeTableRowConvertor {

	CustomizeTableRowDTO convertE2DTO(CustomizeTableRowE customizeTableRowE);

	CustomizeTableRowE convertDTO2E(CustomizeTableRowDTO customizeTableRowDTO);

	List<CustomizeTableRowDTO> convertEList2DTOList(List<CustomizeTableRowE> customizeTableRowEList);

	List<CustomizeTableRowE> convertDTOList2EList(List<CustomizeTableRowDTO> customizeTableRowDTOList);

}