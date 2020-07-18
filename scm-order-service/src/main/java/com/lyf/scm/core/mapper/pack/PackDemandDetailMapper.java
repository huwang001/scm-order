package com.lyf.scm.core.mapper.pack;

import com.lyf.scm.core.domain.entity.pack.PackDemandDetailE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackDemandDetailMapper {

    /**
     * 根据需求单查询 明细
     * @param recordCode
     * @return
     */
    List<PackDemandDetailE> queryDemandDetailByRequireCode(@Param("recordCode") String recordCode);

    /**
     * 根据需求单号列表 批量查询 明细
     *
     * @param recordCodeList
     * @return
     */
    List<PackDemandDetailE> queryDemandDetailByRequireCodeList(@Param("list") List<String> recordCodeList);

    /**
     * 根据id删除成品商品明细
     * @param id
     */
    void  deleteFinishProductSkuDetailById(@Param(value = "id") Long id);

    /**
     * 批量更新需求明细中 实际已包装数量
     * @param demandDetailE
     */
    void batchUpdateDemandDetailActualPackedQty(List<PackDemandDetailE> demandDetailE);

    /**
     * 批量增加需求单成品商品信息
     * @param detailES
     */
    void  insertFinishProduct(List<PackDemandDetailE> detailES );

    /**
     * 根据商品编码集合查询成品明细
     * @param skuCodes
     * @return
     */
    List<PackDemandDetailE> queryDemandDetailBySkuCodes(List<String> skuCodes);

    /**
     * 根据需求单code删除成品商品信息
     * @param recordCode
     */
    void deletePackDemandDetailByRequireCode(@Param("recordCode") String recordCode);
}