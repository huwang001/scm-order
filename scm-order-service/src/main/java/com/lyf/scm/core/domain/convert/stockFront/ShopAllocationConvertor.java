package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.BatchStockDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopAllocationRecordPageDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description 门店调拨单 -对象转换工具
 * @date 2020/6/15
 * @Version
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopAllocationConvertor {

    ShopAllocationE convertDTO2E(ShopAllocationRecordDTO dto);

    ShopAllocationE allocationPageDtoToShopAllocationEntity(ShopAllocationRecordPageDTO pageDTO);

    List<ShopAllocationRecordPageDTO> shopAllocationEntityListToAllocationPageList(List<ShopAllocationE> list);

    List<ShopAllocationDetailDTO> convertDetailE2DetailDTO(List<ShopAllocationDetailE> list);

    ShopAllocationRecordDTO convertE2DTO(ShopAllocationE shopAllocationE);

    List<FrontRecordE> convertE2E(List<ShopAllocationE> list);
}

