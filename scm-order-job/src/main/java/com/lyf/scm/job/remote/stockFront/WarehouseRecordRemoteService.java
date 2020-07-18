package com.lyf.scm.job.remote.stockFront;

import com.lyf.scm.job.api.dto.stockFront.WarehouseRecordPageDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 后置单 出库单查询
 */
@FeignClient(value = "scm-order-service")
public interface WarehouseRecordRemoteService {

    /**
     * 处理 出库单 同步到派单系统
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/order/v1/warehouse_record/handleDispatchCarWarehouseRecord", method = RequestMethod.POST)
    Response<String> handleDispatchCarWarehouseRecord(@RequestParam("id") Long id);


    /**
     * 分页查询 未同步到派单系统的 出库单
     *
     * @param startPage
     * @param endPage
     * @return
     */
    @RequestMapping(value = "/order/v1/warehouse_record/queryNeedSyncTmsBWarehouseRecords", method = RequestMethod.GET)
    Response<List<Long>> queryNeedSyncTmsBWarehouseRecords(@RequestParam("startPage") Integer startPage, @RequestParam("endPage") Integer endPage);

    @RequestMapping(value = "/order/v1/warehouse_record/queryNeedSyncTradeRecordByPage", method = RequestMethod.POST)
    Response<List<WarehouseRecordPageDTO>> queryNeedSyncTradeRecordByPage(@RequestParam("page") int page, @RequestParam("maxResult") int maxResult);

    @RequestMapping(value = "/order/v1/warehouse_record/processSyncTradeStatus", method = RequestMethod.POST)
    Response processSyncTradeStatus(@RequestBody WarehouseRecordPageDTO warehouseRecordPageDTO);
}
