package com.lyf.scm.admin.remote.stockFront;

import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.stockFront.dto.StoreDTO;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "scm-order-service")
public interface RealWarehouseRemoteService {

    @RequestMapping(value = "/order/v1/real_warehouse/queryRealWarehouseByFactoryCode", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCode(@RequestBody String factoryCode);

    /**
     * 根据工厂code查询仓库-非门店
     *
     * @param factoryCode
     * @return
     */
    @RequestMapping(value = "/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeNoShop", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeNoShop(@RequestBody String factoryCode);

    /**
     * 获取所有的非门店仓
     *
     * @return
     */
    @RequestMapping(value = "/order/v1/real_warehouse/queryNotShopWarehouse", method = RequestMethod.GET)
    Response<List<RealWarehouse>> queryNotShopWarehouse();


    /**
     * 根据仓库类型查询仓库信息-不包含门店
     *
     * @return
     */
    @RequestMapping(value = "/order/v1/real_warehouse/queryNotShopWarehouseByType", method = RequestMethod.GET)
    Response<List<RealWarehouse>> queryNotShopWarehouseByType(@RequestParam("realWarehouseType") Integer realWarehouseType);

    /**
     * 获取所有的非门店仓
     *
     * @return
     */
    @RequestMapping(value = "/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeNoShop", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeNoShop();

    /**
     * 根据工厂code和仓库类型查询仓库信息
     */
    @RequestMapping(value = "/order/v1/real_warehouse/queryRealWarehouseByFactoryCodeAndRWType", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndRWType(@RequestParam("factoryCode") String factoryCode, @RequestParam("realWarehouseType") Integer realWarehouseType);

}