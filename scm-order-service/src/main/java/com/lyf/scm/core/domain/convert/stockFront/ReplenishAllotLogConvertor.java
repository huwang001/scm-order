package com.lyf.scm.core.domain.convert.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ReplenishAllotLogDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopReplenishReportDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.ShopReplenishReportStatDTO;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishAllotLogE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.lyf.scm.core.domain.entity.stockFront.ShopReplenishReportStatE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description 补货寻源日志
 * @date 2020/6/19
 */

@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface ReplenishAllotLogConvertor {

    ReplenishAllotLogDTO covertE2DTO(ReplenishAllotLogE replenishAllotLogE);

    ShopReplenishReportStatDTO covertStateE2DTO(ShopReplenishReportStatE statE);

    List<ShopReplenishReportDetailDTO>  covertReList2Elist(List<ReplenishRecordE> eList);

}
