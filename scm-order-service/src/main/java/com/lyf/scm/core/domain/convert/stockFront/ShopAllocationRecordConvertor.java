package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.domain.entity.stockFront.ShopAllocationE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

/**
 * 门店调拨 对象转换工具
 *
 * @author zhanglong
 * @date 2020/7/16 19:35
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopAllocationRecordConvertor {

    CommonFrontRecordDTO convertE2CommonRecordDTO(ShopAllocationE shopAllocationE);

}

