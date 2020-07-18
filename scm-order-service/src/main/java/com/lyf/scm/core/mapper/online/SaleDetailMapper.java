package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.entity.online.SaleDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author <lyf>
 * @version 2019-04-22 15:52:43
 */

public interface SaleDetailMapper {
    /**
     * 保存销售前置单详情
     * 保存字段：
     */
    void saveFrSaleRecordDetails(@Param("frSaleDOList") List<SaleDetailE> frSaleDOList);

    /**
     * 根据ID查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    SaleDetailE selectFrSaleRecordDetailById(Long id);

    /**
     * 查询,根据单据codes
     *
     * @param recordCodes
     * @return
     */
    List<SaleDetailE> selectFrSaleRecordDetailByCodes(@Param("recordCodes") List<String> recordCodes);

}
