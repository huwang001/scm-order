package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/3
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ReplenishDetailConvert {

    List<CommonFrontRecordDetailDTO> convertETOCommDTO(List<ReplenishDetailE> list);
}
