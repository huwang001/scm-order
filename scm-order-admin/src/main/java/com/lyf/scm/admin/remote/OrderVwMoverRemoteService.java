package com.lyf.scm.admin.remote;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.order.OrderVwMoveDTO;
import com.lyf.scm.admin.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.admin.remote.dto.VirtualWarehouse;
import com.rome.arch.core.clientobject.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "scm-order-service")
public interface OrderVwMoverRemoteService {



    @ApiOperation(value = "通过工厂编码和仓库编码查询虚仓列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/order/v1/recordStatusLog/queryVmListByCodes", method = RequestMethod.POST)
    Response<List<VirtualWarehouse>> queryVmListByCodes(@ApiParam(name = "factoryCode", value = "工厂编码") @RequestParam(name = "factoryCode") String factoryCode,
                                                               @ApiParam(name = "realWarehouseOutCode", value = "仓库编码") @RequestParam(name = "realWarehouseOutCode") String realWarehouseOutCode);

    @ApiOperation(value = "查询预约单待虚仓调拨Sku明细", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/order/v1/recordStatusLog/queryNeedOrderVmMoveInfo", method = RequestMethod.POST)
    Response<PageInfo<OrderVwMoveDetailDTO>> queryNeedOrderVmMoveInfo(@ApiParam(name = "orderCode", value = "预约单号") @RequestParam(name = "orderCode") String orderCode,
                                                                      @ApiParam(name = "vwWarehouseCode", value = "虚仓code") @RequestParam(name = "vwWarehouseCode") String vwWarehouseCode,
                                                                             @ApiParam(name = "pageNum", value = "当前页码") @RequestParam("pageNum") Integer pageNum,
                                                                             @ApiParam(name = "pageSize", value = "每页记录数") @RequestParam("pageSize") Integer pageSize);


    /**
     * 保存预约单虚仓调拨信息
     * @param orderVwMoveDTO
     * @return
     */
    @RequestMapping(value = "/order/v1/recordStatusLog/saveNeedOrderVmMoveInfo", method = RequestMethod.POST)
    Response saveNeedOrderVmMoveInfo(@RequestBody OrderVwMoveDTO orderVwMoveDTO);

    /**
     * 查询虚仓信息
     * @param virtualWarehouseCodes
     * @return
     */
    @ApiResponse(code = 200, message = "success")
    @RequestMapping(value = "/order/v1/recordStatusLog/queryVirtualWarehouseByCodes", method = RequestMethod.POST)
    public Response<List<VirtualWarehouse>> queryVirtualWarehouseByCodes( @RequestBody List<String> virtualWarehouseCodes);

}
