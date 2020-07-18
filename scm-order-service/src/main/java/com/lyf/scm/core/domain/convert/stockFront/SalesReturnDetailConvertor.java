package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.SaleWarehouseRecordDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店零售退货明细
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface SalesReturnDetailConvertor {

    List<SaleWarehouseRecordDetailDTO> recordEToRecordDTO(List<WarehouseRecordDetailE> detailEList);

}