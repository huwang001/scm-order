package com.lyf.scm.admin.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.OrderExcelDTO;
import com.lyf.scm.admin.dto.order.OrderVwMoveDTO;
import com.lyf.scm.admin.dto.order.OrderVwMoveDetailAndOrderDTO;
import com.lyf.scm.admin.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.admin.remote.OrderRemoteService;
import com.lyf.scm.admin.remote.dto.OrderRespDTO;
import com.lyf.scm.admin.remote.dto.VirtualWarehouse;
import com.lyf.scm.admin.remote.facade.OrderVwMoverFacade;
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
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;



/**
 * @Description: 虚仓转移controller
 * <p>
 * @Author: chuwenchao  2020/4/8
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/admin/recordStatusLog")
@Api(tags = {"预约单虚仓转移接口管理"})
public class OrderVwMoveController {

    @Resource
    private OrderVwMoverFacade orderVwMoverFacade;

    @Resource
    private OrderRemoteService orderRemoteService;

//    @ApiOperation(value = "通过工厂编码和仓库编码查询虚仓列表", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiResponse(code = 200, message = "success")
//    @RequestMapping(value = "/queryVmListByCodes", method = RequestMethod.POST)
//    public Response<List<VirtualWarehouse>> queryVmListByCodes(@ApiParam(name = "factoryCode", value = "工厂编码") @RequestParam(name = "factoryCode") String factoryCode,
//                                                               @ApiParam(name = "realWarehouseOutCode", value = "仓库编码") @RequestParam(name = "realWarehouseOutCode") String realWarehouseOutCode) {
//        try {
//            List<VirtualWarehouse> list = orderVwMoveService.queryVmListByCodes(factoryCode, realWarehouseOutCode);
//            return Response.builderSuccess(list);
//        } catch (RomeException e) {
//            log.error(e.getMessage(), e);
//            return Response.builderFail(e.getCode(),e.getMessage());
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
//        }
//    }

    @ApiOperation(value = "查询预约单待虚仓调拨Sku明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryNeedOrderVmMoveInfo", method = RequestMethod.GET)
    public Response<OrderVwMoveDetailAndOrderDTO> queryNeedOrderVmMoveInfo(@RequestParam(name = "orderCode", required = false) String orderCode,
                                                                           @ApiParam(name = "vwWarehouseCode", value = "虚仓Code") @RequestParam(name = "vwWarehouseCode") String vwWarehouseCode,
                                                                           @ApiParam(name = "pageNum", value = "当前页码") @RequestParam("pageNum") Integer pageNum,
                                                                           @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam("pageSize") Integer pageSize,HttpServletRequest request) {
        try {
            Response<OrderRespDTO> resultOrder = orderRemoteService.queryOrder(orderCode);
            OrderRespDTO orderDO = resultOrder.getData();

            OrderVwMoveDetailAndOrderDTO orderVwMoveDetailAndOrderDTO = new OrderVwMoveDetailAndOrderDTO();
            orderVwMoveDetailAndOrderDTO.setFactoryCode(orderDO.getFactoryCode());
            orderVwMoveDetailAndOrderDTO.setNeedPackage(orderDO.getNeedPackage());
            orderVwMoveDetailAndOrderDTO.setRealWarehouseCode(orderDO.getRealWarehouseCode());
            orderVwMoveDetailAndOrderDTO.setRealWarehouseName(orderDO.getRealWarehouseName());
            orderVwMoveDetailAndOrderDTO.setJoinVmWarehouseCode(orderDO.getVmWarehouseCode());

            List<VirtualWarehouse> virtualWarehouseListinfo = orderVwMoverFacade.queryVirtualWarehouseByCodes(new ArrayList<>(Arrays.asList(orderDO.getVmWarehouseCode())));
            if (virtualWarehouseListinfo != null && virtualWarehouseListinfo.size() > 0) {
                orderVwMoveDetailAndOrderDTO.setJoinVmWarehouseName(virtualWarehouseListinfo.get(0).getVirtualWarehouseName());
            }


           //删除与调入虚仓编号相同的仓库

            List<VirtualWarehouse> virtualWarehouseList = orderVwMoverFacade.queryVmListByCodes(orderDO.getFactoryCode(), orderDO.getRealWarehouseCode());
            if (virtualWarehouseList != null) {
                for (VirtualWarehouse virtualWarehouse : virtualWarehouseList) {
                    if (orderVwMoveDetailAndOrderDTO.getJoinVmWarehouseCode().equals(virtualWarehouse.getVirtualWarehouseCode())) {
                        virtualWarehouseList.remove(virtualWarehouse);
                        break;
                    }
                }
            }


            //将选择的仓库默认排在第一位
            VirtualWarehouse firstVrtualWarehouse = null;
            for (VirtualWarehouse virtualWarehouse : virtualWarehouseList) {
                if (vwWarehouseCode.equals(virtualWarehouse.getVirtualWarehouseCode())) {
                    virtualWarehouseList.remove(virtualWarehouse);
                    firstVrtualWarehouse = virtualWarehouse;
                    break;
                }
            }
            if(firstVrtualWarehouse!=null) {
                virtualWarehouseList.add(0, firstVrtualWarehouse);
            }

            orderVwMoveDetailAndOrderDTO.setExitVmWarehouseCodeList(virtualWarehouseList);

            PageInfo<OrderVwMoveDetailDTO> result = orderVwMoverFacade.queryNeedOrderVmMoveInfo(orderCode, vwWarehouseCode, pageNum, pageSize);
            if(result!=null){
                for(OrderVwMoveDetailDTO orderVwMoveDetailDTO :result.getList()) {
                    if(orderVwMoveDetailDTO.getVmStockQty()==null){
                        orderVwMoveDetailDTO.setVmStockQty(BigDecimal.ZERO);
                    }

                    if(orderVwMoveDetailDTO.getNeedMoveQty()==null){
                        orderVwMoveDetailDTO.setNeedMoveQty(BigDecimal.ZERO);
                    }
                    if (orderVwMoveDetailDTO.getVmStockQty().compareTo(orderVwMoveDetailDTO.getNeedMoveQty()) <= 0) {//
                        orderVwMoveDetailDTO.setMoveQty(orderVwMoveDetailDTO.getNeedMoveQty());
                    }else{
                        orderVwMoveDetailDTO.setMoveQty(orderVwMoveDetailDTO.getVmStockQty());
                    }
                }
            }
            orderVwMoveDetailAndOrderDTO.setPagelist(result);


            return Response.builderSuccess(orderVwMoveDetailAndOrderDTO);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, ResCode.ORDER_ERROR_1003_DESC);
        }
    }

    @ApiOperation(value = "保存预约单虚仓调拨信息", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/saveNeedOrderVmMoveInfo", method = RequestMethod.POST)
    public Response saveNeedOrderVmMoveInfo(@RequestBody OrderVwMoveDTO orderVwMoveDTO) {
        try {
            Long userId = UserContext.getUserId();
            orderVwMoveDTO.setCreator(userId);
            orderVwMoveDTO.setModifier(userId);
            orderVwMoverFacade.saveNeedOrderVmMoveInfo(orderVwMoveDTO);
            return Response.builderSuccess("保存成功");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC,ResCode.ORDER_ERROR_1003_DESC);
        }
    }

}
