package com.lyf.scm.core.remote.stock;

import com.lyf.scm.core.remote.stock.dto.*;
import com.rome.arch.core.clientobject.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: 库存仓库基础信息查询远程调用接口
 * <p>
 * @Author: chuwenchao  2020/6/15
 */
@FeignClient(name = "stock-inner-app")
public interface StockRealWarehouseRemoteService {

    @RequestMapping(value = "/stock/v1/stockQuery/getChannelSalesByChannelCode", method = RequestMethod.POST)
    Response<ChannelSales> getChannelSalesByChannelCode(@RequestParam("channelCode") String channelCode);

    @RequestMapping(value = "/stock/v1/stockQuery/getChannelSalesByChannelCodes", method = RequestMethod.POST)
    Response<List<ChannelSales>> queryByChannelCodes(@RequestParam("channelCodes") List<String> channelCodes);

    @RequestMapping(value = "/stock/v1/stockQuery/queryByOutCodeAndFactoryCodeList", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryByWarehouseCodeAndFactoryCode(@RequestBody List<QueryRealWarehouseDTO> list);

    @RequestMapping(value = "/stock/v1/stockQuery/queryWmsConfigByFactoryCodeAndOutCode", method = RequestMethod.POST)
    Response<List<RealWarehouseWmsConfig>> queryWmsConfigByWarehouseCodeAndFactoryCode(@RequestBody List<QueryRealWarehouseDTO> list);

    @RequestMapping(value = "/stock/v1/stockQuery/queryVwByRealWarehouseCode", method = RequestMethod.POST)
    Response<List<VirtualWarehouse>> queryVwByRealWarehouseCode(@RequestParam("realWarehouseOutCode") String realWarehouseOutCode, @RequestParam("factoryCode") String factoryCode);

    @RequestMapping(value = "/stock/v1/stockQuery/queryVirtualWarehouseByCodes", method = RequestMethod.POST)
    Response<List<VirtualWarehouse>> queryVirtualWarehouseByCodes(@RequestParam("virtualWarehouseCodes") List<String> vwCodes);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseIdByShopCodes", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByShopCodes(@RequestBody List<String> shopCodes);

    @RequestMapping(value = "/stock/v1/stockQuery/queryVmSkuPermitByGroupIdsAndSkuIds", method = RequestMethod.POST)
    Response<List<VmSkuPermit>> queryVmSkuPermitByGroupIdsAndSkuIds(@RequestBody QueryVmSkuPermitDTO queryVmSkuPermitDTO);

    @RequestMapping(value = "/stock/v1/stockQuery/querySyncRateBySkuIdWId", method = RequestMethod.POST)
    Response<List<SkuRealVirtualStockSyncRelation>> querySyncRateBySkuIdWId(@RequestBody QuerySkuSyncRateDTO querySkuSyncRateDTO);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehousesByFactoryCodes", method = RequestMethod.GET)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodes(@RequestParam("factoryCodes") List<String> factoryCodes);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByFactoryCodeNoShop", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodesNoShop(@RequestParam("factoryCode") String factoryCode);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByFactoryCodeAndRealWarehouseType", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndRWType(@RequestParam("factoryCode") String factoryCode, @RequestParam("type") Integer types);

    @RequestMapping(value = "/stock/v1/stockQuery/queryNotShopWarehouse", method = RequestMethod.GET)
    Response<List<RealWarehouse>> queryNotShopWarehouse();

    @RequestMapping(value = "/stock/v1/stockQuery/queryByType", method = RequestMethod.GET)
    Response<List<RealWarehouse>> queryNotShopWarehouseByType(@RequestParam("type") Integer type);

    @RequestMapping(value = "/stock/v1/stockQuery/queryVmByVirtualGroupCodes", method = RequestMethod.GET)
    Response<List<VirtualWarehouse>> queryVmListByVmGroupCodes(@RequestParam("virtualGroupCodes") List<String> virtualGroupCodes);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByRWCode", method = RequestMethod.POST)
    Response<RealWarehouse> queryRealWarehouseByRWCode(@RequestParam("realWarehouseCode") String realWarehouseCode);

    @RequestMapping(value = "/stock/v1/stockQuery/queryVmByChannelCode", method = RequestMethod.GET)
    Response<List<VirtualWarehouse>> queryVmByChannelCode(@RequestParam("channelCode") String channelCode);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByShopCodeAndType", method = RequestMethod.GET)
    Response<RealWarehouse> queryRealWarehouseByShopCodeAndType(@RequestParam("shopCode") String shopCode, @RequestParam("type") Integer type);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByVmCode", method = RequestMethod.POST)
    Response<RealWarehouse> queryRealWarehouseByVmCode(@RequestParam("virtualWarehouseCode") String virtualWarehouseCode);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByFactoryCodeAndType", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndType(@RequestBody WarehouseQueryDTO warehouseQueryDTO);

    @RequestMapping(value = "/stock/v1/stockQuery/queryRealWarehouseByFactoryCodeAndRealWarehouseType", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryRealWarehouseByFactoryCodeAndRealWarehouseType(@RequestParam("factoryCode") String factoryCode, @RequestParam("type") Integer type);

    @RequestMapping(value = "/stock/v1/stockQuery/queryByOutCodeAndFactoryCodeList", method = RequestMethod.POST)
    Response<List<RealWarehouse>> queryByOutCodeAndFactoryCodeList(@RequestBody List<QueryRealWarehouseDTO> queryRealWarehouseDTOS);

}