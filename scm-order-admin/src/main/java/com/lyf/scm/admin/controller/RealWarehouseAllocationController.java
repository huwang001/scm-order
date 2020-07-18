package com.lyf.scm.admin.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lyf.scm.admin.dto.order.OrderDetailAndAllotDTO;
import com.lyf.scm.admin.dto.order.OrderDetailRespDTO;
import com.lyf.scm.admin.remote.OrderRemoteService;
import com.lyf.scm.admin.remote.dto.OrderRespDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.dto.RealWarehouseBatchAllocationDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouseBatchAllocationQueryDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouseBatchAllocationResDTO;
import com.lyf.scm.admin.remote.facade.RwAllocationFacade;
import com.lyf.scm.common.constants.ResCode;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;
import com.rome.user.common.utils.UserContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;



/**
 * 实仓调拨
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/realWarehouse/allocation")
@Api(tags={"实仓调拨"})
public class RealWarehouseAllocationController {

    @Resource
    private RwAllocationFacade rwAllocationFacade;

    @Resource
    private OrderRemoteService orderRemoteService;

    @ApiOperation(value = "根据工厂编号和仓库类型查询对应仓库", nickname = "queryRealWarehouseByFactoryCodeAndType", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryRealWarehouseByFactoryCodeAndType", method = RequestMethod.POST)
    public Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndType(@ApiParam(name = "realWarehouseBatchAllocationQueryDTO")@RequestBody RealWarehouseBatchAllocationQueryDTO realWarehouseBatchAllocationQueryDTO
            ){
        try {
            List<RealWarehouse> list = rwAllocationFacade.queryRealWarehouseByFactoryCodeAndType(realWarehouseBatchAllocationQueryDTO.getFactoryCode(),realWarehouseBatchAllocationQueryDTO.getNeedPackage().intValue());
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }



    @ApiOperation(value = "查询生成调拨单", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryCreateAllot", method = RequestMethod.GET)
    public Response<OrderDetailAndAllotDTO> queryCreateAllot(@RequestParam(name = "orderCode",required = false) String orderCode, HttpServletRequest request) {
        try {
            Response<OrderRespDTO> resultOrder =  orderRemoteService.queryOrder(orderCode);
            OrderRespDTO orderDO = resultOrder.getData();

            OrderDetailAndAllotDTO orderDetailAndAllotDTO = new OrderDetailAndAllotDTO();
            orderDetailAndAllotDTO.setFactoryCode(orderDO.getFactoryCode());
            orderDetailAndAllotDTO.setNeedPackage(orderDO.getNeedPackage());
            orderDetailAndAllotDTO.setRealWarehouseCode(orderDO.getRealWarehouseCode());
            orderDetailAndAllotDTO.setRealWarehouseName(orderDO.getRealWarehouseName());
            List<RealWarehouse> warehouseList =  rwAllocationFacade.queryRealWarehouseByFactoryCodeAndType(orderDO.getFactoryCode(),orderDO.getNeedPackage());
            orderDetailAndAllotDTO.setWarehouseList(warehouseList);
            Response<List<OrderDetailRespDTO>> result = orderRemoteService.orderDetail(orderCode);
            orderDetailAndAllotDTO.setList(result.getData());
            return Response.builderSuccess(orderDetailAndAllotDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,e.getMessage());
        }
    }


    @ApiOperation(value = "实仓调拨", nickname = "realWarehouseAllocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/realWarehouseAllocation", method = RequestMethod.GET)
    public Response realWarehouseAllocation(@ApiParam(name = "orderCode", value = "预约单号") @RequestParam(name = "orderCode") String orderCode
            ,@ApiParam(name = "realWarehouseCode", value = "仓库外部编号") @RequestParam(name = "realWarehouseCode") String realWarehouseCode){
        try {
        	Long userId = UserContext.getUserId();
        	rwAllocationFacade.realWarehouseAllocation(orderCode, realWarehouseCode, userId);
            return Response.builderSuccess(true);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "实仓批量调拨", nickname = "realWarehouseBatchAllocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/realWarehouseBatchAllocation", method = RequestMethod.POST)
    public Response<List<String>> realWarehouseBatchAllocation(@ApiParam(name = "realWarehouseBatchAllocationDTOList", value = "批量调拨DTO") @RequestBody List<RealWarehouseBatchAllocationDTO> realWarehouseBatchAllocationDTOList){
        try {
            RealWarehouseBatchAllocationResDTO res=rwAllocationFacade.realWarehouseBatchAllocation(realWarehouseBatchAllocationDTOList);
            return Response.builderSuccess(res);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
