package com.lyf.scm.core.mapper.online;

import com.lyf.scm.core.domain.entity.online.RecordPoolE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecordPoolMapper {

    void insertRecordPool(RecordPoolE rwRecordPoolDo);

    /**
     * 根据前置单ID查询发货单信息
     *
     * @param frontRecordCode
     * @return
     */
    List<RecordPoolE> queryByFrontRecordId(@Param("frontRecordCode") String frontRecordCode);

    /**
     * 根据doCode查询发货单信息
     *
     * @param doCode
     * @return
     */
    RecordPoolE queryByDoCode(@Param("doCode") String doCode);

    List<RecordPoolE> queryWaitSyncDoCodes(@Param("page") int page, @Param("maxResult") int maxResult);

    /**
     * 根据doCode列表查询单据池单据信息
     */
    List<RecordPoolE> queryByDoCodes(@Param("doCodes") List<String> doCodes);

    /**
     * 根据doCode列表查询单据池单据信息,用于指定合单
     */
    List<RecordPoolE> queryByDoCodesForMerge(@Param("doCodes") List<String> doCodes);

    /**
     * 根据前置单Code查询发货单信息
     *
     * @param frontRecordCode
     * @return
     */
    List<RecordPoolE> queryNotCanceledByFrontRecordCode(@Param("frontRecordCode") String frontRecordCode);

    /**
     * 根据前置单ID查询发货单信息
     *
     * @param frontRecordCode
     * @return
     */
    List<RecordPoolE> queryNotCanceledByFrontRecordId(@Param("frontRecordCode") String frontRecordCode);

    /**
     * 分页拉取未合单Do池数据
     *
     * @param page      分页页数
     * @param maxResult 结果集最大条数
     * @return
     */
    List<RecordPoolE> queryAllPreMerged(@Param("page") int page, @Param("maxResult") int maxResult, @Param("needCombine") Integer needCombine);

    /**
     * 更新Do池订单状态为待合并
     *
     * @param frontRecordCode
     * @return
     */
    int updateToPreMerge(@Param("frontRecordCode") String frontRecordCode);

    /**
     * 更新DO单池状态为待合单,从已合单状态更新为待合单
     *
     * @param id
     * @return
     */
    int updateToPreMergeById(@Param("id") Long id);

    int updateToPreMergeAndLogisticInfo(RecordPoolE rwRecordPoolDo);

    /**
     * 更新Do池订单状态为已取消
     *
     * @param frontRecordCode
     * @return
     */
    int updateToCanceled(@Param("frontRecordCode") String frontRecordCode);

    /**
     * 根据子do单id更新池内数据状态为已取消
     * @param id 子单id
     * @return
     */
    int updateToCanceledById(@Param("id") Long id);

    /**
     * 取消订单，解锁库存，根据出入库单主键，将已合单DO池数据状态更新为待合单
     */
    int updateToPreMergeByWarehouseRecordId(@Param("warehouseRecordId") Long warehouseRecordId);

    /**
     * 更新MD5指纹信息
     */
    int updateMergeFingerprint(@Param("id") Long id, @Param("mergeFingerprint") String mergeFingerprint);

    /**
     * 合单后，回写Do池
     *
     * @param id
     * @param warehouseRecordId
     * @param warehouseRecordCode
     * @return
     */
    int updateToMerged(@Param("id") Long id, @Param("warehouseRecordId") Long warehouseRecordId, @Param("warehouseRecordCode") String warehouseRecordCode, @Param("versionNo") Integer versionNo);

    int updateVersionNo(@Param("id") Long id, @Param("versionNo") Integer versionNo);

    /**
     * 根据出库单ID查询相关联Do池ID集合
     */
    List<RecordPoolE> queryKeysByWarehouseId(@Param("warehouseRecordId") Long warehouseRecordId);

    /**
     * @param onlineIds
     * @return
     * @Description: 根据后置单ID批量查询未取消DO <br>
     * @Author chuwenchao 2019/9/18
     */
    List<RecordPoolE> queryKeysByWarehouseIds(@Param("onlineIds") List<Long> onlineIds);

    List<RecordPoolE> queryKeysByWarehouseCodes(@Param("codes") List<String> codes);

    /**
     * 更新单据的出库仓库及出库单号
     *
     * @param rwRecordPoolDo
     * @return
     */
    int updateNewRecordInfo(RecordPoolE rwRecordPoolDo);

    int updateTohasSync(Long id);

    int batchUpdateTohasSync(@Param("ids") List<Long> ids);

    /**
     * 更新DO单池状态为待合单 修改物流信息和仓库信息
     *
     * @param rwRecordPoolDo
     * @return
     */
    int updateRwInfoAndLogisticInfo(RecordPoolE rwRecordPoolDo);

    List<RecordPoolE> queryPoolByWarehouseRecordCodes(@Param("warehouseRecordCodes") List<String> warehouseRecordCodes);

    List<Long> queryPoolWarehouseRecordIdByLogisticsCodeList(@Param("logisticsCodeList") List<String> expressCodeList);
}
