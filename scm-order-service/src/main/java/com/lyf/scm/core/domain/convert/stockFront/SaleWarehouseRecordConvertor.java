package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface SaleWarehouseRecordConvertor {

    SaleWarehouseRecordDTO warehouseRecordE2SaleDTO(WarehouseRecordE warehouseRecordE);

    List<SaleWarehouseRecordDTO> warehouseConvertE2DTOList(List<WarehouseRecordE> warehouseRecordEList);

    List<SaleWarehouseRecordDetailDTO> convertEList2SaleWarehouseDTOList(List<WarehouseRecordDetailE> warehouseRecordDetailEList);
}