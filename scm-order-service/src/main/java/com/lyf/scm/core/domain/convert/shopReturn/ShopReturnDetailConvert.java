package com.lyf.scm.core.domain.convert.shopReturn;

import com.lyf.scm.core.api.dto.orderReturn.OrderReturnDetailDTO;
import com.lyf.scm.core.api.dto.shopReturn.ShopReturnDetailDTO;
import com.lyf.scm.core.domain.entity.orderReturn.OrderReturnDetailE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopReturnDetailConvert {
    ShopReturnDetailE convertDTO2E(ShopReturnDetailDTO shopReturnDetailDTO);

    List<ShopReturnDetailE> convertDTOList2EList(List<ShopReturnDetailDTO> orderReturnDetailDTOList);
}
