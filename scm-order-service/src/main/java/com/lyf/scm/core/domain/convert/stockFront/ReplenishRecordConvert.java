package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopReplenishDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 门店补货对象转换
 * <p>
 * @Author: chuwenchao  2020/6/12
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ReplenishRecordConvert {

    ReplenishRecordE convertDTO2E(ShopReplenishDTO shopReplenishDTO);

    List<FrontRecordE> convertETOE(List<ReplenishRecordE> list);

    CommonFrontRecordDTO convertE2CommDTO(ReplenishRecordE replenishRecordE);

}
