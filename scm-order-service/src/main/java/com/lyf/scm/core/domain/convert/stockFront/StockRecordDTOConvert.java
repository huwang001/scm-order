package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.lyf.scm.core.remote.stock.dto.RecordDetailDTO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @Description: 库存出入库对象转换
 * <p>
 * @Author: chuwenchao  2020/6/20
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface StockRecordDTOConvert {

    @Mappings({
            @Mapping(source = "realWarehouseCode", target = "warehouseCode"),
            @Mapping(source = "virtualWarehouseCode", target = "virWarehouseCode"),
            @Mapping(source = "warehouseRecordDetailList", target = "detailList"),
            @Mapping(source = "sapOrderCode", target = "sapPoNo"),
    })
    OutWarehouseRecordDTO convertE2OutDTO(WarehouseRecordE warehouseRecordE);

    @Mappings({
            @Mapping(source = "planQty", target = "basicSkuQty"),
            @Mapping(source = "unit", target = "basicUnit"),
            @Mapping(source = "unitCode", target = "basicUnitCode")
    })
    RecordDetailDTO convertDetailE2OutDTO(WarehouseRecordDetailE warehouseRecordDetailE);

    List<RecordDetailDTO> convertEList2OutDTOList(List<WarehouseRecordDetailE> warehouseRecordDetailE);
}
