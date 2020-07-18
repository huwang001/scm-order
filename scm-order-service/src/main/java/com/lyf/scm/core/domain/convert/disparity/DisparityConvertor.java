package com.lyf.scm.core.domain.convert.disparity;

import com.lyf.scm.core.api.dto.disparity.BatchRefusedBackDTO;
import com.lyf.scm.core.api.dto.disparity.DisparityDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.domain.entity.disparity.DisparityDetailE;
import com.lyf.scm.core.domain.entity.disparity.DisparityRecordE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;
import com.lyf.scm.core.domain.model.disparity.DisparityDetailDO;
import com.lyf.scm.core.domain.model.disparity.DisparityRecordDO;
import com.lyf.scm.core.remote.stock.dto.BatchResultDTO;
import com.lyf.scm.core.remote.stock.dto.OutWarehouseRecordDTO;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**  
 * @ClassName: DisparityConvert  
 * @Description: 差异订单转换类  
 * @author: Lin.Xu  
 * @date: 2020-7-10 21:14:21
 * @version: v1.0
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface DisparityConvertor {


    DisparityRecordDO entityToDo(DisparityRecordE entity);

    DisparityDetailDO entityToDo(DisparityDetailE entity);
    
    List<DisparityDetailE> dtoToEntityForList(List<DisparityDetailDTO> dtoList);

    List<DisparityDetailDTO> entityDetailListToDoList(List<DisparityDetailE> recordDetailEList);
    
    //RomeToLocal
    BatchRefusedBackDTO brDtoTobackDto(BatchResultDTO brdto);
    
    List<BatchRefusedBackDTO> brDtoTobackDtoList(List<BatchResultDTO> brdtos);

    CommonFrontRecordDTO convertE2CommDTO(DisparityRecordE disparityRecordE);

}
