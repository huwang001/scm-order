package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopReplenishReportDTO;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishDetailE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.lyf.scm.core.domain.entity.stockFront.ShopReplenishReportStatE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReplenishRecordDetailMapper {
    
    /**
     * @Description: 保存门店补货单明细 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishDetailE
     * @return 
     */
    void insertDetail(@Param("detail") ReplenishDetailE replenishDetailE);

    /**
     * @Description: 通过单据ID删除明细 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param frontId
     * @return 
     */
    int deleteDetailByFrontId(@Param("frontId") Long frontId);

    /**
     * @Description: 修改明细sap Po单号 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param recordId
     * @return 
     */
    int updateDetailSapPoCode(@Param("recordId") Long recordId, @Param("sapPoNo") String sapPoNo);

    /**
     * @Description: 根据订单IDS查询明细列表 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param recordIds
     * @return 
     */
    List<ReplenishDetailE> queryDetailByRecordIds(@Param("recordIds") List<Long> recordIds);

    /**
     * @Description: 根据订单ID查询明细列表 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param recordId
     * @return
     */
    List<ReplenishDetailE> queryDetailByRecordId(@Param("recordId") Long recordId);

    /**
     * @Description: 批量修改分配数量 <br>
     *
     * @Author chuwenchao 2020/6/17
     * @param replenishDetailEList
     * @return 
     */
    void updateAllotDetail(@Param("replenishDetailEList") List<ReplenishDetailE> replenishDetailEList);

    /**
     * 寻源结果报表
     */
    List<ReplenishRecordE>  queryReplenishReportCondition(@Param("condition") ShopReplenishReportDTO condition);

    /**
     * 交货单汇总
     */
    List<ShopReplenishReportStatE> statReplenishReport(@Param("condition") ShopReplenishReportDTO condition);

    /**
     * @Description: 更新实际出库数量 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param updateDetail
     * @return 
     */
    void updateRealOutQty(@Param("frontRecordDetails") List<ReplenishDetailE> updateDetail);

    /**
     * @Description: 更新实际入库数量 <br>
     *
     * @Author chuwenchao 2020/6/29
     * @param updateDetail
     * @return 
     */
    void updateRealInQty(@Param("frontRecordDetails") List<ReplenishDetailE> updateDetail);
}
