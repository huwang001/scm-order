package com.lyf.scm.core.domain.convert.common;

import java.util.List;

import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.common.RecordStatusLogDTO;
import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 单据状态流转日志对象转换器
 * <p>
 * @Author: wwh 2020/3/5
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface RecordStatusLogConvert {

	RecordStatusLogDTO convertE2DTO(RecordStatusLogE recordStatusLogE);

    List<RecordStatusLogDTO> convertEList2DTOList(List<RecordStatusLogE> recordStatusLogEList);
}