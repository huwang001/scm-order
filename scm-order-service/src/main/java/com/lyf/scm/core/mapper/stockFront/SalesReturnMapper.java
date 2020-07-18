package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.domain.entity.stockFront.SalesReturnE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalesReturnMapper {

    /**
     * 根据外部订单号查询
     *
     * @param outRecordCode
     * @return
     * @throws Exception
     */
    SalesReturnE selectFrSalesReturnRecordByOutRecordCode(String outRecordCode);

    /**
     * 保存退货单
     * 保存字段：record_code,channel_id,channel_type,merchant_id,record_type,record_status,record_status_reason,
     * in_real_warehouse_id,reason,mobile,out_record_code,out_create_time,user_code,shop_code
     */
    void saveFrSalesReturnRecord(SalesReturnE entity);

    /**
     * 取消前置单
     *
     * @param id
     * @param afterRecordStatus
     * @return
     */
    int updateToCanceled(@Param("id") Long id, @Param("afterRecordStatus") Integer afterRecordStatus);

    /**
     * 前置单Id批量查询
     *
     * @param idList
     * @return
     */
    List<SalesReturnE> queryFrontRecordByIds(@Param("idList") List<Long> idList);
}