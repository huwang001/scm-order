package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.model.online.WDTSaleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */

public interface WDTSaleMapper {
    /**
     * 保存
     */
    void saveFrSaleRecord(WDTSaleDO entity);

    /**
     * 根据ID查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    WDTSaleDO selectFrSaleRecordById(Long id);

    /**
     * 根据outRecordCode查找
     *
     * @param outRecordCode
     * @return
     * @throws Exception
     */
    WDTSaleDO selectFrSaleRecordByOutRecordCode(String outRecordCode);

    WDTSaleDO selectFrSaleRecordByRecordCode(String recordCode);

    List<WDTSaleDO> selectFrSaleRecordByRecordCodes(@Param("recordCodes") List<String> recordCodes);

    /**
     * 根据前置SO单id状态为已取消
     *
     * @param id 前置SO单主键
     * @return
     */
    int updateToCanceled(@Param("id") Long id);

    int updateVersion(@Param("id") Long id, @Param("version") Integer version);

    /**
     * 根据前置SO单id状态为已出库
     *
     * @param id 前置SO单主键
     * @return
     */
    int updateToOutAllocation(@Param("id") Long id);

    int saveLogisticsCode(@Param("id") Long id, @Param("logisticsCode") String logisticsCode);

    /**
     * 修改单据信息,重新寻源用
     *
     * @param entity
     * @return
     */
    int updateOrderForRecalHouse(WDTSaleDO entity);


    /**
     * 更新单据拆单状态为已完成
     *
     * @param id
     */
    void updateToHasSplitForOrder(@Param("id") Long id);

    /**
     * 分页查询前置单
     * @param wdtPageParamDTO
     * @return
     */
    //List<WDTPageInfoDO> queryForPage(WDTPageParamDTO wdtPageParamDTO);

    /**
     * 修改单据信息,改仓和物流公司用
     *
     * @param entity
     * @return
     */
    int updateOrderForChangeHouse(WDTSaleDO entity);

    /**
     * 查询wdt出库单编码跟前置单的关系map
     * @param condition
     * @return
     */
    //List<WdtWarehouseRecordDTO> queryWdtFrSaleFrontAndWarehouseRelation(@Param("condition") SaleWarehouseRecordCondition condition);

    /**
     * 根据id集合查询零售单据
     *
     * @param idList
     * @return
     */
    List<WDTSaleDO> queryFrontRecordByIds(@Param("idList") List<Long> idList);

}
