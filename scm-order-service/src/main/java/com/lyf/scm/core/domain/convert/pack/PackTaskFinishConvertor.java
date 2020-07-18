package com.lyf.scm.core.domain.convert.pack;

import com.lyf.scm.core.api.dto.pack.PackTaskOperationDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishResponseDTO;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface PackTaskFinishConvertor {

    @Mappings({
            @Mapping(source = "userId", target = "creator")
    })
    PackTaskFinishE packTaskOperationDto2E(PackTaskOperationDTO operationDTO);

    List<PackTaskFinishE> packTaskOperationDto2EList(List<PackTaskOperationDTO> operationDTOList);

    List<TaskFinishResponseDTO> finishE2DTo(List<PackTaskFinishE> operationDTO);
}
