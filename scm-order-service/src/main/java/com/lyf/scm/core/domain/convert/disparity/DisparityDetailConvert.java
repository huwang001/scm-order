package com.lyf.scm.core.domain.convert.disparity;

import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDetailDTO;
import com.lyf.scm.core.domain.entity.disparity.DisparityDetailE;
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
public interface DisparityDetailConvert {

    List<CommonFrontRecordDetailDTO> convertEList2CommDTOList(List<DisparityDetailE> partDisparityDetailES);
}
