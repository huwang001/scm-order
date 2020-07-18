package com.lyf.scm.core.domain.convert.stockFront;

import java.util.List;

import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDetailDTO;
import org.mapstruct.Mapper;

import com.lyf.scm.core.api.dto.stockFront.WhAllocationDTO;
import com.lyf.scm.core.api.dto.stockFront.WhAllocationDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationDetailE;
import com.rome.arch.core.domain.EntityFactory;

/**
 * @Description: 仓库调拨单明细对象转换器
 * <p>
 * @Author: wwh 2020/5/20
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface WhAllocationDetailConvertor {

    WhAllocationDetailDTO convertE2DTO(WhAllocationDetailE whAllocationDetailE);

    WhAllocationDetailE convertDTO2E(WhAllocationDetailDTO whAllocationDetailDTO);

    List<WhAllocationDetailDTO> convertEList2DTOList(List<WhAllocationDetailE> whAllocationDetailEList);

    List<WhAllocationDetailE> convertDTOList2EList(List<WhAllocationDTO> whAllocationDetailDTOList);

    List<CommonFrontRecordDetailDTO> convertEList2CommDTOList(List<WhAllocationDetailE> whAllocationDetailEList);

}