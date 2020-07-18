package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.InventoryRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopInventoryPageDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ShopInventoryE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店盘点调整单
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopInventoryConvertor {

    ShopInventoryE inventoryDtoToShopInventoryEntity(InventoryRecordDTO frontRecord);

    List<ShopInventoryE> shopInventoryDtoListToShopInventoryEntityList(List<InventoryRecordDTO> dtoList);

    List<ShopInventoryDetailDTO> shopInventoryDetailListDtoToShopInventoryDetailListEntity(List<ShopInventoryDetailE> details);

    ShopInventoryE shopInventoryPageDtoToShopInventoryEntity(ShopInventoryPageDTO sip);

    ShopInventoryPageDTO shopInventoryEntityToShopInventoryPage(ShopInventoryE inventoryE);

    List<ShopInventoryPageDTO> shopInventoryListEntityToShopInventoryPageList(List<ShopInventoryE> inventoryES);

    List<FrontRecordE> convertETOEntity(List<ShopInventoryE> list);
}