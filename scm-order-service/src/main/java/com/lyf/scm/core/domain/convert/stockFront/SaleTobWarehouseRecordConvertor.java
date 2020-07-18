package com.lyf.scm.core.domain.convert.stockFront;


import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface SaleTobWarehouseRecordConvertor {

    SaleTobWarehouseRecordDetailDTO convertEToSaleTobDetailDto(WarehouseRecordDetailE warehouseRecordDetailE);


}
