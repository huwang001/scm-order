package com.lyf.scm.admin.remote.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.stockFront.dto.WarehouseRecordDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WarehouseRecordDetailDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *
 * @author sunyj 2019/4/18 21:08
 */
@FeignClient(value = "scm-order-service")
public interface WarehouseRecordRemoteService {

    @RequestMapping(value = "/order/v1/warehouse_record/in_warehouse_record/queryInWarehouseRecordList", method = RequestMethod.POST)
    Response<PageInfo<WarehouseRecordDTO>> queryInWarehouseRecordList(@RequestBody WarehouseRecordDTO warehouseRecord);

    @RequestMapping(value = "/order/v1/warehouse_record/out_warehouse_record/queryOutWarehouseRecordList", method = RequestMethod.POST)
    Response<PageInfo<WarehouseRecordDTO>> queryOutWarehouseRecordList(@RequestBody WarehouseRecordDTO warehouseRecord);

    @RequestMapping(value = "/order/v1/warehouse_record/warehouse_record/queryWarehouseRecordDetailList", method = RequestMethod.GET)
    Response<List<WarehouseRecordDetailDTO>> queryWarehouseRecordDetailList(@RequestParam("warehouseRecordId") Long warehouseRecordId);
}
