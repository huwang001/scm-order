package com.lyf.scm.core.service.pack;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.pack.*;
import com.lyf.scm.core.domain.entity.pack.PackDemandE;

import java.util.List;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/6
 */
public interface PackDemandService {

    /**
     * 根据需求单code
     * 同步需求单包装完成状态
     * @return void
     * @author huangyl
     * @date 2020/7/14
     * @param recordCode
     */
    void updatePackComplete(String recordCode);

    /**
     * 根据预约单SO
     * 创建单个需求单
     * @return String
     * @author huangyl
     * @date 2020/7/14
     * @param demandFromSoDTO
     */

    String createPackDemandBySo(DemandFromSoDTO demandFromSoDTO);

    /**
     * 页面单个创建需求单
     *
     * @param * @param demandDTO
     * @return java.lang.String
     * @author Lucky
     * @date 2020/7/7  13:52
     */
    String createPackDemand(DemandDTO demandDTO);

    /**
     * 导入创建需求单
     *
     * @param * @param demandDTOList
     * @return java.lang.String
     * @author Lucky
     * @date 2020/7/7  17:08
     */
    void batchCreatePackDemand(List<DemandBatchDTO> demandBatchDTOList, Long userId);

    /**
     * 根据历史需求号返回成品明细和组件明细
     *
     * @param * @param recordCode
     * @return com.lyf.scm.core.api.dto.pack.PackDemandResponseDTO
     * @author Lucky
     * @date 2020/7/7  21:31
     */
    PackDemandResponseDTO queryDemandDetailAndComponent(String recordCode);



    /**
     * 根据需求单号
     * 单个修改单据状态为已确认，并下发包装系统
     * @return void
     * @author huangyl
     * @date 2020/7/14
     * @param recordCode
     */
    void  updateDemandRecordStatusConfirmed(String recordCode,Long userId);

    /**
     * @param packDemandE
     */
    void releasePackSystem(PackDemandE packDemandE);

    /**
     * 分页查询需求单列表
     * @author huwang
     * @data 2020/7/7  17:31
     * @param condition
     * @return com.lyf.scm.core.api.dto.pack.PackDemandResponseDTO
     */
    PageInfo<PackDemandResponseDTO> queryPackDemandPage(QueryPackDemandDTO condition);

    /**
     * 需求单导出-查询
     * @param condition
     * @return
     */
    List<PackDemandResponseDTO> queryPackDemandExport(QueryPackDemandDTO condition);

    /**
     * 查询需求单详情
     * @author huwang
     * @data 2020/7/7  21:31
     * @param recordCode
     * @return com.lyf.scm.core.api.dto.pack.PackDemandResponseDTO
     */
    PackDemandResponseDTO queryPackDemandDetail(String recordCode);

    /**
     * 取消需求单
     * @param requireCode
     * @return
     */
    void cancelPackDemand(String requireCode);

    /**
     * 查询需求单列表
     * @author huwang
     * @data 2020/7/7  17:31
     * @param condition
     * @return com.lyf.scm.core.api.dto.pack.PackDemandResponseDTO
     */
    PageInfo<PackDemandResponseDTO> queryPackDemandList(QueryPackDemandDTO condition);
}
