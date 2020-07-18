package com.lyf.scm.core.api.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.common.constants.ResCode;
import com.lyf.scm.common.util.ListUtil;
import com.lyf.scm.core.api.dto.order.OrderVwMoveDTO;
import com.lyf.scm.core.api.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.core.config.ScmCallLog;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;
import com.lyf.scm.core.remote.stock.facade.StockRealWarehouseFacade;
import com.lyf.scm.core.service.order.OrderVwMoveService;
import com.rome.arch.core.clientobject.Response;
import com.rome.arch.core.exception.RomeException;
import com.rome.arch.util.controller.RomeController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 虚仓转移controller
 * <p>
 * @Author: chuwenchao  2020/4/8
 */
@Slf4j
@RomeController
@RequestMapping("/order/v1/recordStatusLog")
@Api(tags = {"预约单虚仓转移接口管理"})
public class OrderVwMoveController {

    @Resource
    private OrderVwMoveService orderVwMoveService;

    @Resource
    private StockRealWarehouseFacade stockRealWarehouseFacade;


    @ApiOperation(value = "通过工厂编码和仓库编码查询虚仓列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryVmListByCodes", method = RequestMethod.POST)
    public Response<List<VirtualWarehouse>> queryVmListByCodes(@ApiParam(name = "factoryCode", value = "工厂编码") @RequestParam(name = "factoryCode") String factoryCode,
                                                               @ApiParam(name = "realWarehouseOutCode", value = "仓库编码") @RequestParam(name = "realWarehouseOutCode") String realWarehouseOutCode) {
        try {
            List<VirtualWarehouse> list = orderVwMoveService.queryVmListByCodes(factoryCode, realWarehouseOutCode);
            return Response.builderSuccess(list);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "查询预约单待虚仓调拨Sku明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryNeedOrderVmMoveInfo", method = RequestMethod.POST)
    public Response<PageInfo<OrderVwMoveDetailDTO>> queryNeedOrderVmMoveInfo(@ApiParam(name = "orderCode", value = "预约单号") @RequestParam(name = "orderCode") String orderCode,
                                                                             @ApiParam(name = "vwWarehouseCode", value = "虚仓Code") @RequestParam(name = "vwWarehouseCode") String vwWarehouseCode,
                                                                             @ApiParam(name = "pageNum", value = "当前页码") @RequestParam("pageNum") Integer pageNum,
                                                                             @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam("pageSize") Integer pageSize) {
        try {
            List<OrderVwMoveDetailDTO> list = orderVwMoveService.queryNeedOrderVmMoveInfo(orderCode, vwWarehouseCode);
            List<OrderVwMoveDetailDTO> pageList = ListUtil.getPageList(list, pageNum, pageSize);
            PageInfo<OrderVwMoveDetailDTO> pageInfo = new PageInfo<>(pageList);
            pageInfo.setPageNum(pageNum);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotal(list.size());
            return Response.builderSuccess(pageInfo);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "保存预约单虚仓调拨信息", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/saveNeedOrderVmMoveInfo", method = RequestMethod.POST)
    @ScmCallLog(systemName = "scm-core-service", recordCode = "#orderVwMoveDTO.orderCode")
    public Response saveNeedOrderVmMoveInfo(@RequestBody OrderVwMoveDTO orderVwMoveDTO) {
        try {
            orderVwMoveService.saveNeedOrderVmMoveInfo(orderVwMoveDTO);
            return Response.builderSuccess("保存成功");
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

    @ApiOperation(value = "查询虚仓信息", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/queryVirtualWarehouseByCodes", method = RequestMethod.POST)
    public Response<List<VirtualWarehouse>> queryVirtualWarehouseByCodes(@RequestBody List<String> virtualWarehouseCodes) {
        try {
            List<VirtualWarehouse> vrtualWarehouseList = stockRealWarehouseFacade.queryVirtualWarehouseByCodes(virtualWarehouseCodes);
            return Response.builderSuccess(vrtualWarehouseList);
        } catch (RomeException e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.builderFail(ResCode.ORDER_ERROR_1003_DESC, e.getMessage());
        }
    }

}
