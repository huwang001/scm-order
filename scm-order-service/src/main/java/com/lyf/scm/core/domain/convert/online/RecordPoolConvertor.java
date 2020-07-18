package com.lyf.scm.core.domain.convert.online;

import com.lyf.scm.core.api.dto.online.RecordPoolDTO;
import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Remarks APP电商-pool转换
 * @date 2020/7/2
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface RecordPoolConvertor {

    List<RecordPoolDTO> convertEList2DTOList(List<RecordPoolE> rwRecordPoolEList);
}
