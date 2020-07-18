package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.model.online.WDTSaleDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */

public interface WDTSaleDetailMapper {
    /**
     * 保存销售前置单详情
     * 保存字段：
     */
    void saveFrSaleRecordDetails(@Param("frSaleDOList") List<WDTSaleDetailDO> frSaleDOList);

    /**
     * 根据ID查找
     *
     * @param frontRecordId
     * @return
     * @throws Exception
     */
    List<WDTSaleDetailDO> selectFrSaleRecordDetailById(Long frontRecordId);

    List<WDTSaleDetailDO> selectFrSaleRecordDetailByIds(@Param("frontRecordIds") List<Long> frontRecordIds);

    /**
     * 根据ID查找
     *
     * @param frontRecordCode
     * @return
     * @throws Exception
     */
    List<WDTSaleDetailDO> selectFrSaleRecordDetailByCode(String frontRecordCode);


    List<WDTSaleDetailDO> selectDetailByIdForSplit(Long frontRecordId);


    /**
     * 根据单号更新明细状态为已拆单完成
     */
    void updateToHasSplitForDetailsByRecordId(@Param("frontRecordId") Long frontRecordId);

    /**
     * 根据明细id集合更新明细状态为已拆单完成
     */
    void updateToHasSplitForDetailsByDetailIds(@Param("ids") List<Long> ids);

    List<Long> selectFrontIdsBySkuCodes(@Param("skuList") List<String> skuList);

    /**
     * 根据明细id集合更新明细状态为已拆单完成,并且数量为0，即取消掉该行
     */
    int updateToCancel(@Param("ids") List<Long> ids);


}
