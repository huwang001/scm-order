package com.lyf.scm.core.mapper.stockFront;

import com.lyf.scm.core.api.dto.stockFront.ShopReplenishAllotDTO;
import com.lyf.scm.core.domain.entity.stockFront.ReplenishRecordE;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReplenishRecordMapper {

    /**
     * @Description: 通过外部单号查询门店补货单 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param outRecordCode
     * @return 
     */
    ReplenishRecordE queryByOutCode(@Param("outRecordCode") String outRecordCode);

    /**
     * @Description: 保存门店补货单 <br>
     *
     * @Author chuwenchao 2020/6/12
     * @param replenishRecordE
     * @return 
     */
    void insertReplenishRecord(ReplenishRecordE replenishRecordE);

    /**
     * @Description: 修改sap Po单号 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param id
     * @param sapPoNo
     * @return 
     */
    int updateSapPoCode(@Param("id") Long id, @Param("sapPoNo") String sapPoNo);

    /**
     * @Description: 批量取消单据 <br>
     *
     * @Author chuwenchao 2020/6/13
     * @param ids
     * @return 
     */
    int updateToCancel(@Param("ids") List<Long> ids);

    /**
     * @Description: 查询待分配的前置单 <br>
     *
     * @Author chuwenchao 2020/6/15
     * @param startRow
     * @param rows
     * @param allotDTO
     * @return
     */
    List<ReplenishRecordE> getWaitAllotRecord(@Param("startRow") Integer startRow, @Param("rows") Integer rows, @Param("allotDTO") ShopReplenishAllotDTO allotDTO);

    /**
     * @Description: 根据前置单ID更新单据寻源状态 <br>
     *
     * @Author chuwenchao 2020/6/17
     * @param frontId
     * @param outWarehouse
     * @param allotStatus
     * @return
     */
    int updateAllotStatus(@Param("frontId") Long frontId, @Param("outWarehouse") RealWarehouse outWarehouse, @Param("allotStatus") Integer allotStatus);

    /**
     * @Description: <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param idList
     * @return 
     */
    List<ReplenishRecordE> queryReplenishRecordByIds(@Param("idList") List<Long> idList);

    /**
     * @Description: 通过ID查询门店补货单 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param id
     * @return 
     */
    ReplenishRecordE queryReplenishRecordById(@Param("id") Long id);

    /**
     * @Description: 更新门店补货前置单据状态为已出库 <br>
     *
     * @Author chuwenchao 2020/6/23
     * @param id
     * @return 
     */
    void updateToOutAllocation(@Param("id") Long id);

    /**
     * 批量修改派车状态完成 0:待指定 -> 1:派车 订单状态 0初始 -> 4已派车
     * @param ids
     * @return
     */
    int updateIsNeedDispatchComplete(@Param("ids") List<Long> ids);


    /**
     * @Description: 更新门店补货前置单据状态为已入库 <br>
     *
     * @Author chuwenchao 2020/6/29
     * @param id
     * @return 
     */
    void updateToInAllocation(@Param("id") Long id);
}
