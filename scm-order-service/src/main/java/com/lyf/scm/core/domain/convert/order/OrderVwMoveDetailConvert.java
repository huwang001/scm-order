package com.lyf.scm.core.domain.convert.order;

import com.lyf.scm.core.api.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.core.domain.entity.order.OrderVwMoveDetailE;
import com.lyf.scm.core.remote.stock.dto.VwDetailDTO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

/**
 * @Description: 虚仓转移明细model转换器 <br>
 *
 * @Author chuwenchao 2020/4/10
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface OrderVwMoveDetailConvert {

    OrderVwMoveDetailDTO convertE2DTO(OrderVwMoveDetailE orderVwMoveDetailE);

    OrderVwMoveDetailE convertDTO2E(OrderVwMoveDetailDTO orderVwMoveDetailDTO);

    OrderVwMoveDetailE convertDetailDTO2E(VwDetailDTO vwDetailDTO);
}
