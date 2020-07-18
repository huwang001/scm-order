package com.lyf.scm.core.service.stockFront;


import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnRecordParamDTO;
import com.lyf.scm.core.api.dto.stockFront.SalesReturnWarehouseRecordDTO;

public interface SalesReturnService {

    /**
     * 创建门店销售退货单
     *
     * @param frontRecord
     * @return
     * @author zhanglong
     */
    void addShopSaleReturnRecord(SalesReturnRecordDTO frontRecord);

    /**
     * 取消门店销售退货单
     *
     * @param outRecordCode
     * @return
     * @author zhanglong
     */
    void cancelShopSaleReturnRecord(String outRecordCode);

    /**
     * 门店零售退货单 列表查询
     *
     * @param paramDTO
     * @return
     * @author zhanglong
     */
    PageInfo<SalesReturnWarehouseRecordDTO> findBySalesReturnRecordCondition(SalesReturnRecordParamDTO paramDTO);

    /**
     * 门店退货详情查询
     *
     * @param recordId
     * @return
     * @author zhanglong
     */
    SalesReturnWarehouseRecordDTO querySaleReturnWarehouseRecordInfoById(Long recordId);
}
