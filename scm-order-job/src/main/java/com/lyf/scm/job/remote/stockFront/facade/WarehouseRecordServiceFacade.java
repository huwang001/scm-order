package com.lyf.scm.job.remote.stockFront.facade;

import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.job.api.dto.stockFront.WarehouseRecordPageDTO;
import com.lyf.scm.job.remote.stockFront.WarehouseRecordRemoteService;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 出库单查询
 */
@Slf4j
@Component
public class WarehouseRecordServiceFacade {

    @Resource
    private WarehouseRecordRemoteService warehouseRecordRemoteService;

    /**
     * 处理出库单 同步到 派车系统
     */
    public void handleDispatchCarWarehouseRecord(Long id) {
        Response<String> response = warehouseRecordRemoteService.handleDispatchCarWarehouseRecord(id);
        ResponseValidateUtils.validResponse(response);
    }

    /**
     * 分页查询 待同步派车系统的 出库单
     *
     * @return
     */
    public List<Long> queryNeedSyncTmsBWarehouseRecords(Integer startPage, Integer endPage) {
        Response<List<Long>> response = warehouseRecordRemoteService.queryNeedSyncTmsBWarehouseRecords(startPage, endPage);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 获取待同步交易的单子
     * @param page
     * @param maxResult
     * @return
     */
    public List<WarehouseRecordPageDTO> queryBySyncTradeStatusByPage(int page, int maxResult){
        Response<List<WarehouseRecordPageDTO>> response = warehouseRecordRemoteService.queryNeedSyncTradeRecordByPage(page, maxResult);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 处理待同步交易的单子
     * @param warehouseRecordPageDTO
     * @return
     */
    public void processSyncTradeStatus(WarehouseRecordPageDTO warehouseRecordPageDTO){
        Response response = warehouseRecordRemoteService.processSyncTradeStatus(warehouseRecordPageDTO);
        ResponseValidateUtils.validResponse(response);
    }

}
