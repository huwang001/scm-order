package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface FrontRecordConvertor {

    List<FrontRecordE> convertETOFrontRecordE(List<WhAllocationE> list);
}
