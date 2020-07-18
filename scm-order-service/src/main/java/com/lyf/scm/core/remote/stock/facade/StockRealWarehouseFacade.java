package com.lyf.scm.core.remote.stock.facade;

import com.google.common.collect.Lists;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.lyf.scm.core.remote.stock.StockRealWarehouseRemoteService;
import com.lyf.scm.core.remote.stock.dto.*;
import com.rome.arch.core.clientobject.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 库存仓库基础信息查询
 * <p>
 * @Author: chuwenchao  2020/6/12
 */
@Slf4j
@Component
@AllArgsConstructor
public class StockRealWarehouseFacade {

    private final int PAGE_SIZE = 100;

    @Resource
    private StockRealWarehouseRemoteService stockRealWarehouseRemoteService;

    /**
     * @param channelCode
     * @return
     * @Description: 单个渠道查询 <br>
     * @Author chuwenchao 2020/6/11
     */
    public ChannelSales queryByChannelCode(String channelCode) {
        Response<ChannelSales> resp = stockRealWarehouseRemoteService.getChannelSalesByChannelCode(channelCode);
        ResponseValidateUtils.validResponse(resp);
        return resp.getData();
    }

    /**
     * @param channelCodes
     * @return
     * @Description: 批量查询渠道 <br>
     * @Author chuwenchao 2020/6/11
     */
    public List<ChannelSales> queryByChannelCodes(List<String> channelCodes) {
        Response<List<ChannelSales>> response = stockRealWarehouseRemoteService.queryByChannelCodes(channelCodes);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据实仓编码、工厂编码查询实仓信息
     *
     * @param factoryCode
     * @return
     */
    public RealWarehouse queryByWarehouseCodeAndFactoryCode(String warehouseCode, String factoryCode) {
        RealWarehouse realWarehouse = null;
        if (StringUtils.isNoneBlank(warehouseCode, factoryCode)) {
            QueryRealWarehouseDTO queryRealWarehouseDTO = new QueryRealWarehouseDTO();
            queryRealWarehouseDTO.setWarehouseOutCode(warehouseCode);
            queryRealWarehouseDTO.setFactoryCode(factoryCode);
            List<QueryRealWarehouseDTO> queryRealWarehouseList = new ArrayList<QueryRealWarehouseDTO>();
            queryRealWarehouseList.add(queryRealWarehouseDTO);
            List<RealWarehouse> realWarehouseList = this.queryByWarehouseCodeAndFactoryCode(queryRealWarehouseList);
            if (CollectionUtils.isNotEmpty(realWarehouseList)) {
                realWarehouse = realWarehouseList.get(0);
            }
        }
        return realWarehouse;
    }

    /**
     * 根据实仓编码、工厂编码集合查询实仓信息列表
     *
     * @param list
     * @return
     */
    public List<RealWarehouse> queryByWarehouseCodeAndFactoryCode(List<QueryRealWarehouseDTO> list) {
        List<RealWarehouse> realWarehouseList = new ArrayList<RealWarehouse>();
        if (CollectionUtils.isNotEmpty(list)) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryByWarehouseCodeAndFactoryCode(list);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }

    /**
     * 根据实仓编码、工厂编码集合查询WMS配置信息列表
     *
     * @param list
     * @return
     */
    public List<RealWarehouseWmsConfig> queryWmsConfigByWarehouseCodeAndFactoryCode(List<QueryRealWarehouseDTO> list) {
        List<RealWarehouseWmsConfig> realWarehouseWmsConfigList = new ArrayList<RealWarehouseWmsConfig>();
        if (CollectionUtils.isNotEmpty(list)) {
            // 集合分割处理
            List<List<QueryRealWarehouseDTO>> partList = ListUtils.partition(list, PAGE_SIZE);
            for (int i = 0; i < partList.size(); i++) {
                List<QueryRealWarehouseDTO> subList = partList.get(i);
                Response<List<RealWarehouseWmsConfig>> resp = stockRealWarehouseRemoteService.queryWmsConfigByWarehouseCodeAndFactoryCode(subList);
                ResponseValidateUtils.validResponse(resp);
                realWarehouseWmsConfigList.addAll(resp.getData());
            }
        }
        return realWarehouseWmsConfigList;
    }

    /**
     * 根据实仓编码、工厂编码查询虚仓列表
     *
     * @param queryVw
     * @return
     */
    public List<VirtualWarehouse> queryVwByRealWarehouseCode(QueryVirtualWarehouseDTO queryVw) {
        List<VirtualWarehouse> virtualWarehouseList = new ArrayList<VirtualWarehouse>();
        if (queryVw != null) {
            Response<List<VirtualWarehouse>> resp = stockRealWarehouseRemoteService.queryVwByRealWarehouseCode(queryVw.getRealWarehouseOutCode(), queryVw.getFactoryCode());
            ResponseValidateUtils.validResponse(resp);
            virtualWarehouseList = resp.getData();
        }
        return virtualWarehouseList;
    }


    /**
     * 根据虚仓编码集合查询虚仓列表
     *
     * @param vwCodes
     * @return
     */
    public List<VirtualWarehouse> queryVirtualWarehouseByCodes(List<String> vwCodes) {
        List<VirtualWarehouse> virtualWarehouseList = new ArrayList<VirtualWarehouse>();
        if (CollectionUtils.isNotEmpty(vwCodes)) {
            Response<List<VirtualWarehouse>> resp = stockRealWarehouseRemoteService.queryVirtualWarehouseByCodes(vwCodes);
            ResponseValidateUtils.validResponse(resp);
            virtualWarehouseList = resp.getData();
        }
        return virtualWarehouseList;
    }

    /**
     * 批量根据门店编号查询实仓信息
     *
     * @param shopCodes
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByShopCodes(List<String> shopCodes) {
        List<RealWarehouse> realWarehouses = new ArrayList<>();
        if (null != shopCodes && shopCodes.size() > 0) {
            // 集合分割处理
            List<List<String>> partList = ListUtils.partition(shopCodes, PAGE_SIZE);
            for (int i = 0; i < partList.size(); i++) {
                List<String> subList = partList.get(i);
                Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryRealWarehouseByShopCodes(subList);
                ResponseValidateUtils.validResponse(resp);
                realWarehouses.addAll(resp.getData());
            }
        }
        return realWarehouses;
    }

    /**
     * 根据门店编号查询实仓信息
     *
     * @param shopCode
     * @return
     */
    public RealWarehouse queryRealWarehouseByShopCode(String shopCode) {
        List<RealWarehouse> realWarehouses = this.queryRealWarehouseByShopCodes(Lists.newArrayList(shopCode));
        if (CollectionUtils.isNotEmpty(realWarehouses)) {
            return realWarehouses.get(0);
        }
        return null;
    }

    /**
     * 根据虚仓组ID集合、skuId集合、是否有权限查询进货权限商品列表
     *
     * @param queryVmSkuPermitDTO
     * @return
     */
    public List<VmSkuPermit> queryVmSkuPermitByGroupIdsAndSkuIds(QueryVmSkuPermitDTO queryVmSkuPermitDTO) {
        List<VmSkuPermit> vmSkuPermitList = new ArrayList<VmSkuPermit>();
        if (queryVmSkuPermitDTO != null) {
            Response<List<VmSkuPermit>> resp = stockRealWarehouseRemoteService.queryVmSkuPermitByGroupIdsAndSkuIds(queryVmSkuPermitDTO);
            ResponseValidateUtils.validResponse(resp);
            vmSkuPermitList = resp.getData();
        }
        return vmSkuPermitList;
    }

    /**
     * 根据skuId和实仓编码、工厂编码查询库存同步比例列表
     *
     * @param skuId
     * @param realWarehouseCode
     * @param factoryCode
     * @return
     */
    public List<SkuRealVirtualStockSyncRelation> querySyncRateBySkuIdWId(Long skuId, String realWarehouseCode, String factoryCode) {
        List<SkuRealVirtualStockSyncRelation> skuRealVirtualStockSyncRelationList = new ArrayList<SkuRealVirtualStockSyncRelation>();
        if (skuId != null && StringUtils.isNoneBlank(realWarehouseCode, factoryCode)) {
            QuerySkuSyncRateDTO querySkuSyncRateDTO = new QuerySkuSyncRateDTO();
            querySkuSyncRateDTO.setSkuId(skuId);
            querySkuSyncRateDTO.setWarehouseOutCode(realWarehouseCode);
            querySkuSyncRateDTO.setFactoryCode(factoryCode);
            Response<List<SkuRealVirtualStockSyncRelation>> resp = stockRealWarehouseRemoteService.querySyncRateBySkuIdWId(querySkuSyncRateDTO);
            ResponseValidateUtils.validResponse(resp);
            skuRealVirtualStockSyncRelationList = resp.getData();
        }
        return skuRealVirtualStockSyncRelationList;
    }

    /**
     * 根据工厂编码集合查询实仓列表
     *
     * @param factoryCodes
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByFactoryCodes(List<String> factoryCodes) {
        List<RealWarehouse> realWarehouseList = new ArrayList<RealWarehouse>();
        if (CollectionUtils.isNotEmpty(factoryCodes)) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryRealWarehouseByFactoryCodes(factoryCodes);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }

    /**
     * 根据工厂编码集合查询实仓列表 -不含门店
     *
     * @param factoryCode
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByFactoryCodesNoShop(String factoryCode) {
        List<RealWarehouse> realWarehouseList = new ArrayList<>();
        if (StringUtils.isNotBlank(factoryCode)) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryRealWarehouseByFactoryCodesNoShop(factoryCode);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }

    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndRWType(String factoryCodes, Integer realWarehouseType) {
        List<RealWarehouse> realWarehouseList = new ArrayList<>();
        if (StringUtils.isNotBlank(factoryCodes)) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryRealWarehouseByFactoryCodeAndRWType(factoryCodes, realWarehouseType);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }

    /**
     * 获取所有非门店仓
     *
     * @return
     */
    public List<RealWarehouse> queryNotShopWarehouse() {
        Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryNotShopWarehouse();
        ResponseValidateUtils.validResponse(resp);
        List<RealWarehouse> realWarehouseList = resp.getData();
        return realWarehouseList;
    }

    /**
     * 根据类型获取所有非门店仓
     *
     * @return
     */
    public List<RealWarehouse> queryNotShopWarehouseByType(Integer type) {
        Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryNotShopWarehouseByType(type);
        ResponseValidateUtils.validResponse(resp);
        List<RealWarehouse> realWarehouseList = resp.getData();
        return realWarehouseList;
    }

    /**
     * @param vmGroupCodes
     * @return
     * @Description: 根据虚仓组查询虚仓列表 <br>
     * @Author chuwenchao 2020/6/11
     */
    public List<VirtualWarehouse> queryVmListByVmGroupCodes(List<String> vmGroupCodes) {
        Response<List<VirtualWarehouse>> resp = stockRealWarehouseRemoteService.queryVmListByVmGroupCodes(vmGroupCodes);
        ResponseValidateUtils.validResponse(resp);
        return resp.getData();
    }

    /**
     * 根据实仓内部编码查询实仓
     *
     * @param realWarehouseCode
     * @return
     */
    public RealWarehouse queryRealWarehouseByRWCode(String realWarehouseCode) {
        if (StringUtils.isNotEmpty(realWarehouseCode)) {
            Response<RealWarehouse> resp = stockRealWarehouseRemoteService.queryRealWarehouseByRWCode(realWarehouseCode);
            ResponseValidateUtils.validResponse(resp);
            return resp.getData();
        }
        return null;
    }

    /**
     * @param channelCode
     * @return
     * @Description: 通过渠道查询虚仓列表 <br>
     * @Author chuwenchao 2020/6/20
     */
    public List<VirtualWarehouse> getVwListByChannelCode(String channelCode) {
        Response<List<VirtualWarehouse>> response = stockRealWarehouseRemoteService.queryVmByChannelCode(channelCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @param shopCode
     * @param rwType
     * @return
     * @Description: 通过门店编码+仓库类型查询门店发货仓 <br>
     * @Author chuwenchao 2020/6/20
     */
    public RealWarehouse queryRealWarehouseByShopCodeAndType(String shopCode, Integer rwType) {
        Response<RealWarehouse> response = stockRealWarehouseRemoteService.queryRealWarehouseByShopCodeAndType(shopCode, rwType);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * @param vmCode
     * @return
     * @Description: 通过虚仓Code查询实仓 <br>
     * @Author chuwenchao 2020/6/23
     */
    public RealWarehouse queryRealWarehouseByVmCode(String vmCode) {
        Response<RealWarehouse> response = stockRealWarehouseRemoteService.queryRealWarehouseByVmCode(vmCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据工厂编号和仓库类型查询对应仓库
     *
     * @param warehouseQueryDTO
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndType(WarehouseQueryDTO warehouseQueryDTO) {
        List<RealWarehouse> realWarehouseList = new ArrayList<>();
        if (warehouseQueryDTO != null) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryRealWarehouseByFactoryCodeAndType(warehouseQueryDTO);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }

    /**
     * 根据工厂编号和仓库类型查询仓库列表
     *
     * @param factoryCode
     * @param type
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndRealWarehouseType(String factoryCode, Integer type) {
        List<RealWarehouse> realWarehouseList = new ArrayList<>();
        if (StringUtils.isNotBlank(factoryCode) && type != null) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryRealWarehouseByFactoryCodeAndRealWarehouseType(factoryCode, type);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }

    /**
     * 批量查询实仓接口
     *
     * @param list
     * @return
     */
    public List<RealWarehouse> queryByOutCodeAndFactoryCodeList(List<QueryRealWarehouseDTO> list) {
        List<RealWarehouse> realWarehouseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            Response<List<RealWarehouse>> resp = stockRealWarehouseRemoteService.queryByOutCodeAndFactoryCodeList(list);
            ResponseValidateUtils.validResponse(resp);
            realWarehouseList = resp.getData();
        }
        return realWarehouseList;
    }


}