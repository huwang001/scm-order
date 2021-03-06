package com.lyf.scm.admin.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.stockFront.dto.WarehouseRecordDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WarehouseRecordDetailDTO;
import com.lyf.scm.admin.remote.stockFront.facade.WarehouseRecordFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.util.controller.RomeController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出入库单
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/warehouse_record")
public class WarehouseRecordController {
    @Autowired
    private WarehouseRecordFacade warehouseRecordFacade;

    @ApiOperation(value = "查询入库单列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/in_warehouse_record/list", method = RequestMethod.POST)
    public Response<PageInfo<WarehouseRecordDTO>> queryInWarehouseRecordList(@RequestBody WarehouseRecordDTO warehouseRecord) {
        try {
            PageInfo<WarehouseRecordDTO> result = warehouseRecordFacade.queryInWarehouseRecordList(warehouseRecord);
            Map<String , Object> res = new HashMap<>();
            if(null != result){
                res.put("list" , result.getList());
                res.put("total" ,result.getTotal() );
            }
            return Response.builderSuccess(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询出库单列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/out_warehouse_record/list", method = RequestMethod.POST)
    public Response<PageInfo<WarehouseRecordDTO>> queryOutWarehouseRecord(@RequestBody WarehouseRecordDTO warehouseRecord) {
        try {
            PageInfo<WarehouseRecordDTO> result = warehouseRecordFacade.queryOutWarehouseRecordList(warehouseRecord);
            Map<String , Object> res = new HashMap<>();
            if(null != result){
                res.put("list" , result.getList());
                res.put("total" ,result.getTotal() );
            }
            return Response.builderSuccess(res);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "查询出入库单明细列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success", response = List.class)
    @RequestMapping(value = "/warehouse_record_detail/list", method = RequestMethod.GET)
    public Response<List<WarehouseRecordDetailDTO>> queryWarehouseRecordDetail(@RequestParam Long warehouseRecordId) {
        try {
            List<WarehouseRecordDetailDTO> result = warehouseRecordFacade.queryWarehouseRecordDetailList(warehouseRecordId);
            return Response.builderSuccess(result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
