package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordCondition;
import com.lyf.scm.core.api.dto.stockFront.SaleTobWarehouseRecordDTO;

import java.util.List;
import java.util.Map;

public interface SaleTobWarehouseRecordService {

    /**
     * 查询发货单
     * @param condition
     * @return
     */
	PageInfo<SaleTobWarehouseRecordDTO> queryWarehouseRecordList(SaleTobWarehouseRecordCondition condition);

    /**
     * 获取已有所需前置单据门店信息
     * @return
     */
    List<Map<String,String>> getShopInfo();

    /**
     * 根据发货单id查看详情s
     * @param warehouseRecordId
     * @return
     */
    SaleTobWarehouseRecordDTO getWarehouseSaleTobDetail(Long warehouseRecordId);

}
