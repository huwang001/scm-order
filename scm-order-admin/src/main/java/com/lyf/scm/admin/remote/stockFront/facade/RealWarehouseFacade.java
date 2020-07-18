package com.lyf.scm.admin.remote.stockFront.facade;

import java.util.List;

import javax.annotation.Resource;

import com.lyf.scm.admin.remote.stockFront.dto.StoreDTO;
import com.lyf.scm.common.enums.RealWarehouseTypeEnum;
import com.rome.arch.core.exception.RomeException;
import org.springframework.stereotype.Component;

import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.stockFront.RealWarehouseRemoteService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RealWarehouseFacade {

    @Resource
    private RealWarehouseRemoteService realWarehouseRemoteService;


    public List<RealWarehouse> queryRealWarehouseByFactoryCode(String factoryCode) {
        Response<List<RealWarehouse>> response = realWarehouseRemoteService.queryRealWarehouseByFactoryCode(factoryCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据工厂code查询仓库-非门店
     *
     * @param factoryCode
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeNoShop(String factoryCode) {
        Response<List<RealWarehouse>> response = realWarehouseRemoteService.queryRealWarehouseByFactoryCodeNoShop(factoryCode);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }


    /**
     * 获取所有的非门店仓
     *
     * @return
     */
    public List<RealWarehouse> queryNotShopWarehouse() {
        Response<List<RealWarehouse>> response = realWarehouseRemoteService.queryNotShopWarehouse();
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据仓库类型获取所有的非门店仓
     *
     * @return
     */
    public List<RealWarehouse> queryNotShopWarehouseByType(Integer realWarehouseType) {
        Response<List<RealWarehouse>> response = realWarehouseRemoteService.queryNotShopWarehouseByType(realWarehouseType);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据工厂code查询仓库-非门店
     *
     * @param factoryCode
     * @return
     */
    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndRWType(String factoryCode, Integer realWarehouseType) {
        Response<List<RealWarehouse>> response = realWarehouseRemoteService.queryRealWarehouseByFactoryCodeAndRWType(factoryCode, realWarehouseType);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

}