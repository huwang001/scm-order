package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordPageDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface WarehouseRecordConvertor {

	WarehouseRecordDTO convertE2DTO(WarehouseRecordE warehouseRecordE);

    WarehouseRecordE convertDTO2E(WarehouseRecordDTO warehouseRecordDTO);
	
    List<WhAllocationDTO> convertEList2DTOList(List<WarehouseRecordE> warehouseRecordEList);
    
    List<WarehouseRecordE> convertDTOList2EList(List<WhAllocationDTO> whAllocationDTOList);
    
    WarehouseRecordE convertE2E(WhAllocationE whAllocationE);

    WarehouseRecordE convertDtoToEntity(WarehouseRecordPageDTO warehouseRecord);

    List<WarehouseRecordPageDTO> convertEToDto(List<WarehouseRecordE> list);

    List<WarehouseRecordDetailDTO> entityToDtoDetails(List<WarehouseRecordDetailE> list);

    List<WarehouseRecordPageDTO> entityToWarehouseDto(List<WarehouseRecordE> warehouseRecordEList);


}