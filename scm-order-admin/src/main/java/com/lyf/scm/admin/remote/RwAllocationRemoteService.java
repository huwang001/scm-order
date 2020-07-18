package com.lyf.scm.admin.remote;

import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(value="scm-order-service")
public interface RwAllocationRemoteService {

    @RequestMapping(value = "/order/v1/realWarehouse/allocation/queryRealWarehouseByFactoryCodeAndType", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndType(@RequestParam(value = "factoryCode")String factoryCode, @RequestParam(value = "type") Integer type);

    @RequestMapping(value = "/order/v1/realWarehouse/allocation/realWarehouseAllocation", method = RequestMethod.POST)
    Response realWarehouseAllocation(@RequestParam(value = "orderCode")String orderCode, @RequestParam(value = "allotRealWarehouseCode") String allotRealWarehouseCode, @RequestParam("userId") Long userId);
}