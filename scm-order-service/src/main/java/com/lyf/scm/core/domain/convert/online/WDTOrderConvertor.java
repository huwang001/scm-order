package com.lyf.scm.core.domain.convert.online;

import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface WDTOrderConvertor {
}
