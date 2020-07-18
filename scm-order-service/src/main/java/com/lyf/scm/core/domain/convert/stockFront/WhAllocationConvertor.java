package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.*;
import com.lyf.scm.core.domain.entity.stockFront.WhAllocationE;
import com.lyf.scm.core.remote.item.dto.ParamExtDTO;
import com.lyf.scm.core.remote.item.dto.SkuUnitExtDTO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 仓库调拨单对象转换器
 * <p>
 * @Author: wwh 2020/5/20
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface WhAllocationConvertor {

    WhAllocationDTO convertE2DTO(WhAllocationE whAllocationE);

    WhAllocationE convertDTO2E(WhAllocationDTO whAllocationDTO);

    List<WhAllocationDTO> convertEList2DTOList(List<WhAllocationE> whAllocationEList);

    List<WhAllocationE> convertDTOList2EList(List<WhAllocationDTO> whAllocationDTOList);

    WhAllocationE convertPageDTO2E(WhAllocationPageDTO whAllocationPageDTO);

    WhAllocationPageDTO convertE2PageDTO(WhAllocationE whAllocationE);

    List<WhAllocationPageDTO> convertEList2PageDTOList(List<WhAllocationE> whAllocationEList);

    List<ParamExtDTO> converTempaleList2UnitParamList(List<WhAllocationTemplateDetailDTO> tempDetailList);

    WhAllocationExportTemplate convertPageDTO2Template(WhAllocationPageDTO whAllocationPageDTO);

    List<WhSkuUnitDTO> convertUnitList2WhUnitList(List<SkuUnitExtDTO> skuUnitExtDTOList);

    CommonFrontRecordDTO convertE2CommDTO(WhAllocationE whAllocationE);

    /**
     * 单位转化
     *
     * @param unitList
     * @return
     */
    List<WhSkuUnitDTO> skuUnitCovertor(List<SkuUnitExtDTO> unitList);

}