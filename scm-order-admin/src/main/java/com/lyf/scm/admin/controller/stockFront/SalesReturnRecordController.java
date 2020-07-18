package com.lyf.scm.admin.controller.stockFront;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.stockFront.dto.SalesReturnRecordParamDTO;
import com.lyf.scm.admin.remote.stockFront.dto.SalesReturnWarehouseRecordDTO;
import com.lyf.scm.admin.remote.stockFront.facade.SalesReturnRecordFacade;
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
@RequestMapping("/order/v1/salesReturn")
public class SalesReturnRecordController {

    @Resource
    private SalesReturnRecordFacade salesReturnRecordFacade;

    @ApiOperation(value = "批量查询退货单", nickname = "find_sales_return_record_condition", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryCondition", method = RequestMethod.POST)
    public Response findBySalesReturnRecordCondition(@RequestBody SalesReturnRecordParamDTO paramDTO) {
        try {
            PageInfo<SalesReturnWarehouseRecordDTO> salesReturnRecordPageInfo = salesReturnRecordFacade.findBySalesReturnRecordCondition(paramDTO);
            return Response.builderSuccess(salesReturnRecordPageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "根据门店退货单id查询详情", nickname = "queryDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryDetail/{recordId}", method = RequestMethod.GET)
    public Response<SalesReturnWarehouseRecordDTO> queryByCondition(@ApiParam(name = "recordId", value = "recordId") @PathVariable Long recordId) {
        try {
            return Response.builderSuccess(salesReturnRecordFacade.querySaleReturnWarehouseRecordInfoById(recordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
}
