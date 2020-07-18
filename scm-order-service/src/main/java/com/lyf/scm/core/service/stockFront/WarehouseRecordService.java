package com.lyf.scm.core.service.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordDetailDTO;
import com.lyf.scm.core.api.dto.stockFront.WarehouseRecordPageDTO;
import com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE;

import java.util.List;

public interface WarehouseRecordService {

    /**
     * 查询后置单信息（包括明细）
     *
     * @param warehouseRecordId
     * @return
     */
    WarehouseRecordE queryWarehouseRecordEById(Long warehouseRecordId);

    /**
     * 查询入库单
     * @param warehouseRecord
     * @return
     */
    PageInfo<WarehouseRecordPageDTO> queryInWarehouseRecordList(WarehouseRecordPageDTO warehouseRecord);

    /**
     * 查询出库单
     * @param warehouseRecord
     * @return
     */
    PageInfo<WarehouseRecordPageDTO> queryOutWarehouseRecordList(WarehouseRecordPageDTO warehouseRecord);

    /**
     * 根据出入单id查询出入库单详情
     * @param warehouseRecordId
     * @return
     */
    List<WarehouseRecordDetailDTO> queryWarehouseRecordDetailList(Long warehouseRecordId);

    /**
     * @Description: 【定时器】分页查询待同步交易单据 <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param page
     * @return
     */
    List<WarehouseRecordPageDTO> queryNeedSyncTradeRecordByPage(Integer page, Integer maxResult);

    /**
     * 【定时器】 查询 待同步到 派车 系统的出库单
     *
     * @param startPage
     * @param endPage
     * @return
     * @author zhanglong
     */
    List<Long> queryNeedSyncTmsBWarehouseRecords(Integer startPage, Integer endPage);

    /**
     * 【定时器】 处理待同步的出库单 到 派车系统
     * @param id
     * @author zhanglong
     */
    void handleDispatchCarWarehouseRecord(Long id);


    /**
     * @Description: 【定时器】 处理待同步交易的单子 <br>
     *
     * @Author chuwenchao 2020/6/19
     * @param warehouseRecord
     * @return
     */
    void processSyncTradeStatus(WarehouseRecordPageDTO warehouseRecord);

    /**
     * 通过入库单编码查询出库单编码
     * 库存中心-直送 场景
     * @param recordCode
     * @return
     * @author zhanglong
     */
    List<String> queryOutWhRecordByInWhRecord(String recordCode);
    
    /**
     * @Description 通过订单号查询后置单信息  包含：对应的明细记录
     * @Author Lin.Xu
     * @Date 9:58 2020/7/17
     * @Param [orderNos]
     * @return java.util.List<com.lyf.scm.core.domain.entity.stockFront.WarehouseRecordE>
     **/
    List<WarehouseRecordE> queryWarehouseRecordByOrderNos(List<String> orderNos);
    
}
