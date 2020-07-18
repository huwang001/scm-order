package com.lyf.scm.core.domain.convert.shopReturn;

import com.lyf.scm.core.api.dto.shopReturn.ShopReturnDTO;
import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDetailDTO;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnDetailE;
import com.lyf.scm.core.domain.entity.shopReturn.ShopReturnE;
import com.lyf.scm.core.domain.entity.stockFront.FrontRecordE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/15
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ShopReturnConvert {

    ShopReturnE convertDTO2E(ShopReturnDTO shopReturnDTO);

    List<FrontRecordE> convertEtoE(List<ShopReturnE> shopReturnES);

    CommonFrontRecordDTO convert2CommonRecord(ShopReturnE shopReturnE);

    CommonFrontRecordDetailDTO convert2CommonRecordDetail(ShopReturnDetailE detailE);

    List<CommonFrontRecordDetailDTO> convert2DetailList(List<ShopReturnDetailE> detailEList);
}
