package com.lyf.scm.admin.controller.stockFront;


import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.SaleTobWarehouseRecordCondition;
import com.lyf.scm.admin.remote.stockFront.dto.BatchCancleDTO;
import com.lyf.scm.admin.remote.stockFront.dto.SaleTobWarehouseRecordDTO;
import com.lyf.scm.admin.remote.stockFront.facade.SaleTobWarehouseRecordFacade;
import com.lyf.scm.admin.remote.stockFront.facade.ShopReplenishFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.user.common.utils.UserContext;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order/v1/warehouse_sale_tob")
public class SaleTobWarehouseRecordController {
    @Autowired
    private SaleTobWarehouseRecordFacade saleTobWarehouseRecordFacade;

    @Autowired
    private ShopReplenishFacade shopReplenishFacade;

    @ApiOperation(value = "查询发货单", nickname = "query_sale_warehouse_record", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageInfo<SaleTobWarehouseRecordDTO>> queryByCondition(@ApiParam(name = "condition", value = "查询条件") @RequestBody SaleTobWarehouseRecordCondition condition) {
        try {
            return Response.builderSuccess(saleTobWarehouseRecordFacade.queryByCondition(condition));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
    @ApiOperation(value = "获取已有所需前置单据门店信息", nickname = "query_sale_warehouse_record", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/getShopInfo", method = RequestMethod.GET)
    public Response<List<Map<String,String>>> getShopInfo() {
        try {
            return Response.builderSuccess(saleTobWarehouseRecordFacade.getShopInfo());
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }
    @ApiOperation(value = "根据发货单id查看详情", nickname = "getWarehouseSaleTobDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/getWarehouseSaleTobDetail", method = RequestMethod.POST)
    public Response<SaleTobWarehouseRecordDTO> getWarehouseSaleTobDetail(@RequestParam("warehouseRecordId") Long warehouseRecordId) {
        try {
            return Response.builderSuccess(saleTobWarehouseRecordFacade.getWarehouseSaleTobDetail(warehouseRecordId));
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "批量取消补货单", nickname = "cancleBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/cancelReplenishBatch", method = RequestMethod.POST)
    public Response<String> cancelReplenishBatch(@ApiParam(name = "String", value = "单号") @RequestBody String list) {
        try {
            Long userId = UserContext.getUserId();
            BatchCancleDTO dto = new BatchCancleDTO();
            dto.setList(list);
            dto.setUserId(userId);
            String msg = shopReplenishFacade.cancelReplenishBatch(dto);
            return Response.builderSuccess(msg);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
