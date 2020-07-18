package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnWarehouseRecordDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.entity.stockFront.SalesReturnE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店零售退货
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface SalesReturnConvertor {

    SalesReturnE salesReturnDtoToEntity(SalesReturnRecordDTO frontRecord);

    List<SalesReturnWarehouseRecordDTO> salesReturnRecordEToDTOList(List<WarehouseRecordE> salesReturnRecordList);

    SalesReturnWarehouseRecordDTO salesReturnRecordEToDTO(WarehouseRecordE warehouseRecord);

    List<FrontRecordE> convertETOE(List<SalesReturnE> list);
}