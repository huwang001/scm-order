package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.api.dto.stockFront.CommonFrontRecordDTO;
import com.lyf.scm.core.remote.stock.dto.CancelRecordDTO;

import java.util.List;

public interface WarehouseRecordCommService {

    /**
     * 取消 库存 出入库单 （内部通过MQ实现）
     *
     * @param cancelRecord
     * @author zhanglong
     */
    void cancelWarehouseRecordToStock(CancelRecordDTO cancelRecord);

    /**
     * 通过doCode出库单号前置单so详情
     *
     * @param * @param doCode
     * @return com.lyf.scm.core.api.dto.common.CommonFrontRecordDTO
     * @author Lucky
     * @date 2020/7/3  13:52
     */
    List<CommonFrontRecordDTO> queryCommonFrontRecordInfoByRecordCode(String doCode);

    /**
     * 通过后置单编码 查询送达方
     *
     * @param recordCode
     * @return
     * @author zhanglong
     */
    String queryRecordReceiveCode(String recordCode, String type);
}
