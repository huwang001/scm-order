package com.lyf.scm.core.mapper.pack;

import com.lyf.scm.core.api.dto.pack.QueryPackDemandDTO;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackDemandMapper {

    /**
     * 通过需求单号 查询需求单信息
     *
     * @param recordCode
     * @return
     */
    PackDemandE queryByRecordCode(String recordCode);

    /**
     * 批量通过需求单号查询需求单信息
     *
     * @param recordCodeList
     * @return
     */
    List<PackDemandE> batchQueryByRecordCode(List<String> recordCodeList);

    /**
     * 单个新增需求单
     *
     * @param * @param packDemandE
     * @return int
     * @author Lucky
     * @date 2020/7/7  11:37
     */
    int insert(PackDemandE packDemandE);

    /**
     * 根据Id修改需求单
     *
     * @param * @param packDemandE
     * @return int
     * @author Lucky
     * @date 2020/7/10  22:53
     */
    int updateById(PackDemandE packDemandE);

    /**
     * 批量增加需求单
     *
     * @param * @param packDemandEList
     * @return int
     * @author Lucky
     * @date 2020/7/7  11:47
     */
    int batchInsert(List<PackDemandE> packDemandEList);

    /**
     * 根据需求单号
     * 修改单据状态为已完成包装
     * @return int
     * @author huangyl
     * @date 2020/7/14
     * @param recordCode
     */
    int updateRecordStatusToCompletePackByRecordCode(@Param("recordCode") String recordCode);

    /**
     * 根据需求单号
     * 修改单据状态为已确认
     * @return int
     * @author huangyl
     * @date 2020/7/14
     * @param recordCode
     */
    int updateRecordStatusToConfirmedByRecordCode(@Param("recordCode") String recordCode);

    /**
     * 更新需求单状态为：部分包装
     *
     * @param id
     */
    void updatePackDemandPartPacked(Long id);

    /**
     * 取消需求单
     *
     * @param id
     * @param beforeStatus
     */
    void updatePackDemandCancel(@Param("id") Long id, @Param("beforeStatus") Integer beforeStatus);

    /**
     * 根据销售单号查询需求单信息
     *
     * @param saleCode
     * @return
     */
    PackDemandE queryBySaleCode(@Param("saleCode") String saleCode);

    /**
     * 分页查询需求单列表
     *
     * @param condition
     */
    List<PackDemandE> queryPackDemandPage(@Param("condition") QueryPackDemandDTO condition);

    /**
     * 根据需求编码修改领料状态
     *
     * @param recordCode
     * @param pickStatus
     * @return
     */
    int updatePickStatusByRecordCode(@Param("recordCode") String recordCode, @Param("pickStatus") Integer pickStatus);

    /**
     * 根据ID查询需求单
     *
     * @param orderLineNo
     * @return
     */
    PackDemandE queryById(@Param("id") Long id);

    /**
     * 根据需求单号和渠道查询需求单
     *
     * @param condition
     * @return
     */
    List<PackDemandE> queryPackDemandList(@Param("condition") QueryPackDemandDTO condition);

    /**
     * 分页查询需求单列表(不含skucode条件)
     *
     * @param condition
     */
    List<PackDemandE> queryPackDemandListByCondition(@Param("condition") QueryPackDemandDTO condition);
}