package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.entity.online.RecordPoolDetailE;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RecordPoolDetailMapper {

    void insertAllRwRecordPoolDetail(@Param("rwRecordPoolDetails") List<RecordPoolDetailE> rwRecordPoolDetails);

    List<RecordPoolDetailE> queryByRecordPoolIds(@Param("ids") List<Long> ids);

    void updateQty(@Param("id") Long id, @Param("skuQty") BigDecimal skuQty, @Param("basicSkuQty") BigDecimal basicSkuQty);

    /**
     * 根据前置单ID批量修改明细的实仓和虚仓
     *
     * @param recordPoolId
     * @param realWarehouseId
     * @param virtualWarehouseId
     */
    void updateDetailRwInfo(@Param("recordPoolId") Long recordPoolId,
                            @Param("realWarehouseId") Long realWarehouseId,
                            @Param("virtualWarehouseId") Long virtualWarehouseId);
}
