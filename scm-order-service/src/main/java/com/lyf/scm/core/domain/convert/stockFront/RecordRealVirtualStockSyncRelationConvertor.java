package com.lyf.scm.core.domain.convert.stockFront;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.stockFront.RecordRealVirtualStockSyncRelationDTO;
import com.lyf.scm.core.domain.entity.stockFront.RecordRealVirtualStockSyncRelationE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 仓库调拨单对象转换器
 *               <p>
 * @Author: wwh 2020/5/20
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface RecordRealVirtualStockSyncRelationConvertor {

	RecordRealVirtualStockSyncRelationDTO convertE2DTO(
			RecordRealVirtualStockSyncRelationE recordRealVirtualStockSyncRelationE);

	RecordRealVirtualStockSyncRelationE convertDTO2E(
			RecordRealVirtualStockSyncRelationDTO recordRealVirtualStockSyncRelationDTO);

	List<RecordRealVirtualStockSyncRelationDTO> convertEList2DTOList(
			List<RecordRealVirtualStockSyncRelationE> recordRealVirtualStockSyncRelationEList);

	List<RecordRealVirtualStockSyncRelationE> convertDTOList2EList(
			List<RecordRealVirtualStockSyncRelationDTO> recordRealVirtualStockSyncRelationDTOList);

}