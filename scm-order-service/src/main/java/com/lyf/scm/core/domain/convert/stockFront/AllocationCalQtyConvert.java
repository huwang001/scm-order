package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.remote.stock.dto.AllocationCalQtyParam;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author leiyi
 * @Description 发货单位取整
 * @date 2020/5/13
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface AllocationCalQtyConvert {

    @Mappings({
            @Mapping(source = "skuQty", target = "planQty"),
            @Mapping(source = "skuScale",target = "scale")
    })
    AllocationCalQtyParam detailToAllocation(ReplenishDetailE replenishDetailE);

    List<AllocationCalQtyParam> detailsToAllocations(List<ReplenishDetailE> replenishDetailEList);

}    
   