package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopReplenishAllotDTO;
import com.lyf.scm.core.domain.entity.stockFront.FrReplenishRecordResultE;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FrReplenishRecordMapper {

    Integer queryNumByOutRecordCode(String outRecordCode);

    long insert(ReplenishRecordE ReplenishRecordE);

    ReplenishRecordE queryByRecordCode(@Param("recordCode") String recordCode);

    ReplenishRecordE queryByOutRecordCode(@Param("outRecordCode") String recordCode);

    ReplenishRecordE queryBySapPoNo(@Param("sapPoNo") String sapPoNo);

    List<FrReplenishRecordResultE> queryByWhere(FrReplenishRecordResultE paramDTO);

    /**
     * 根据ID查询前置单
     *
     * @param id
     * @return
     */
    ReplenishRecordE getById(@Param("id") Long id);

    /**
     * 修改前置单单据为收货完成
     *
     * @return
     */
    int updateToDelivery(@Param("id") Long id);

    /**
     * 更新加盟门店补货前置单据状态为已派车
     */
    int updateToTMS(@Param("id") Long id, @Param("isNeedDispatch") Integer isNeedDispatch);


    /**
     * 更新直送门店为已入库状态
     */
    int updateInitToAllocation(@Param("id") Long id);

    /**
     * 更新门店补货前置单据状态为已出库
     */
    int updateToOutAllocation(@Param("id") Long id);


    /**
     * 查询待分配的前置单
     *
     * @param currentPage
     * @param rows
     * @param allotDTO
     * @param allotValue
     * @return
     */
    List<ReplenishRecordE> getWaitAllotRecord(@Param("page") int currentPage, @Param("rows") int rows, @Param("allotValue") Integer allotValue, @Param("allotDTO") ShopReplenishAllotDTO allotDTO);

    /**
     * 修改前置单单据为分配成功
     *
     * @return
     */
    int updateToAllotSucc(@Param("id") Long id);

    /**
     * 修改前置单单据为分配失败
     *
     * @return
     */
    int updateToAllotFailList(@Param("ids") List<Long> ids);

    /**
     * 修改前置单单据为取消
     * @param idList
     * @return
     */
    int updateToCancle(@Param("idList") List<Long> idList);


    /**
     * 更新出库仓库ID
     *
     * @param id
     * @param realWarehouse
     * @return
     */
    int updateOutWarehouseId(@Param("id") Long id, @Param("realWarehouse") RealWarehouse realWarehouse);


    /**
     * 更新出库仓库ID和寻源状态
     *
     * @param id
     * @param outWarehouseId
     * @param allotStatus
     * @return
     */
    int updateOutWhIdAndAllotStatus(@Param("id") Long id, @Param("outWarehouseId") Long outWarehouseId, @Param("allotStatus") Integer allotStatus);

    /**
     * 通过ID集合批量查询
     *
     * @param ids
     * @return
     */
    List<ReplenishRecordE> getByIds(@Param("ids") List<Long> ids);

    /**
     * 修改sapPoCode
     *
     * @param id
     * @param sapPoNo
     * @return
     */
    int updateSapPoCode(@Param("id") Long id, @Param("sapPoNo") String sapPoNo);

    List<ReplenishRecordE> queryFrontRecordByIds(@Param("idList") List<Long> idList);

    List<String> queryFrontRecordCodesByInShopCode(@Param("inShopCode") String inShopCode, @Param("sapPoNos") List<String> sapPoNos, @Param("outRecordCode") String outRecordCode);

    List<ReplenishRecordE> queryFrontRecordByRecordCodes(@Param("frontRecordCodes") List<String> frontRecordCodes);

    List<Map<String,String>> getShopInfo();
}
