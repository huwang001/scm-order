package com.lyf.scm.core.service.stockFront;

import com.lyf.scm.core.remote.base.dto.StoreDTO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;
import io.swagger.models.auth.In;

import java.util.List;

public interface RealWarehouseService {

    /**
     * 根据工厂code查询仓库-含门店
     *
     * @param factoryCode
     * @return
     */
    List<RealWarehouse> queryRealWarehouseByFactoryCode(String factoryCode);

    /**
     * 根据工厂code查询仓库-不含门店
     *
     * @param factoryCode
     * @return
     */
    List<RealWarehouse> queryRealWarehouseByFactoryCodeNoShop(String factoryCode);

    /**
     * 查询仓库-不含门店
     *
     * @param * @param
     * @return java.util.List<com.lyf.scm.core.remote.stock.dto.RealWarehouse>
     * @author Lucky
     * @date 2020/7/10  11:53
     */
    List<RealWarehouse> queryNotShopWarehouse();

    /**
     * 通过仓库类型查询仓库-不含门店
     *
     * @param * @param
     * @return java.util.List<com.lyf.scm.core.remote.stock.dto.RealWarehouse>
     * @author Lucky
     * @date 2020/7/10  11:53
     */
    List<RealWarehouse> queryNotShopWarehouseByType(Integer type);

    /**
     * 根据工厂code和仓库类型查询仓库
     *
     * @param factoryCode
     * @param realWarehouseType
     * @return
     */
    List<RealWarehouse> queryRealWarehouseByFactoryCodeAndRWType(String factoryCode, Integer realWarehouseType);


}
