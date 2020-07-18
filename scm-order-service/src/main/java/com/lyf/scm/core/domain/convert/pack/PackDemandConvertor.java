package com.lyf.scm.core.domain.convert.pack;

import com.lyf.scm.core.api.dto.pack.DemandBatchDTO;
import com.lyf.scm.core.api.dto.pack.DemandDTO;
import com.lyf.scm.core.api.dto.pack.PackDemandResponseDTO;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Remarks
 * @date 2020/7/6
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface PackDemandConvertor {

    PackDemandE convertorDTO2E(DemandDTO demandDTO);

    List<PackDemandE> convertorDTOList2EList(List<DemandDTO> demandDTOList);

    List<PackDemandResponseDTO> convertEListToDtoList(List<PackDemandE> packDemandEList);

    PackDemandResponseDTO convertEToDto(PackDemandE packDemandE);

    List<ParamExtDTO> convertBatchDTOLitsToParamDTOList(List<DemandBatchDTO> demandBatchDTOList);

    DemandDTO convertBatchDTOToDTO(DemandBatchDTO demandBatchDTOList);
}
