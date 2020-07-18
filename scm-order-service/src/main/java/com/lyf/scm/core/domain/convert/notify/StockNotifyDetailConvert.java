package com.lyf.scm.core.domain.convert.notify;

import com.lyf.scm.core.api.dto.notify.StockNotifyDetailDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordDetailE;
import com.rome.arch.core.domain.EntityFactory;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: 库存回调通知对象转换
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
@Mapper(componentModel = "spring", uses = EntityFactory.class)
public interface StockNotifyDetailConvert {

    WarehouseRecordDetailE convertNotifyToRecordDetail(StockNotifyDetailDTO notifyDetailDTO);

    List<WarehouseRecordDetailE> convertNotifyToRecordDetailList(List<StockNotifyDetailDTO> notifyDetailDTOList);
}

