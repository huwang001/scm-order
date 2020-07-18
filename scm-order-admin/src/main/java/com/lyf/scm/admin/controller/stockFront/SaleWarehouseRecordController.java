package com.lyf.scm.admin.controller.stockFront;


import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.SaleWarehouseRecordCondition;
import com.lyf.scm.admin.remote.stockFront.dto.SaleWarehouseRecordDTO;
import com.lyf.scm.admin.remote.stockFront.facade.SaleWarehouseRecordFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/order/v1/warehouse_sale")
public class SaleWarehouseRecordController {

    @Resource
    private SaleWarehouseRecordFacade saleWarehouseRecordFacade;

    @ApiOperation(value = "", nickname = "query_sale_warehouse_record", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageInfo<SaleWarehouseRecordDTO>> queryByCondition(@ApiParam(name = "condition", value = "查询条件")
                                                                       @RequestBody SaleWarehouseRecordCondition condition) {
        try {
            PageInfo<SaleWarehouseRecordDTO> list = saleWarehouseRecordFacade.queryByCondition(condition);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "跟据门店销售出库单id查询详情", nickname = "query_sale_warehouse_record_detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/querySaleWarehouseRecordInfo/{recordId}", method = RequestMethod.GET)
    public Response<SaleWarehouseRecordDTO> queryByCondition(@ApiParam(name = "recordId", value = "recordId") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(saleWarehouseRecordFacade.querySaleWarehouseRecordInfoById(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
