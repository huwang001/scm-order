package com.lyf.scm.core.domain.convert.online;

import com.lyf.scm.core.domain.entity.online.WDTSaleE;
import com.lyf.scm.core.domain.model.online.WDTSaleDO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

/**
 * @Desc:销售单-转换
 * @author:Huangyl
 * @date: 2020/7/2
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface WDTSaleConvertor {

    WDTSaleE wdtSaleDOTowdtSaleE(WDTSaleDO wdtSaleDO);
}
