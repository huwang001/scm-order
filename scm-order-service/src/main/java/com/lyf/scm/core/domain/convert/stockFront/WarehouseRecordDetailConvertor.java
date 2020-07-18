package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface WarehouseRecordDetailConvertor {

    WarehouseRecordDetailDTO convertE2DTO(WarehouseRecordDetailE warehouseRecordDetailE);
	
    WarehouseRecordDetailE convertDTO2E(WarehouseRecordDetailDTO warehouseRecordDetailDTO);
	
    List<WhAllocationDetailDTO> convertEList2DTOList(List<WarehouseRecordDetailE> warehouseRecordDetailEList);

    List<WarehouseRecordDetailE> convertDTOList2EList(List<WhAllocationDetailDTO> whAllocationDetailDTOList);

}
