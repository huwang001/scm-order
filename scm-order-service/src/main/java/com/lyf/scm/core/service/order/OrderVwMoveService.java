package com.lyf.scm.core.service.order;

import com.lyf.scm.core.api.dto.order.OrderVwMoveDTO;
import com.lyf.scm.core.api.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.core.remote.stock.dto.VirtualWarehouse;

import java.util.List;

public interface OrderVwMoveService {

    /**
     * @Description: 通过工厂编码和仓库编码查询虚仓列表 <br>
     *
     * @Author chuwenchao 2020/4/8
     * @param factoryCode
     * @param realWarehouseOutCode
     * @return 
     */
    List<VirtualWarehouse> queryVmListByCodes(String factoryCode, String realWarehouseOutCode);

    /**
     * @Description: 查询预约单待虚仓调拨Sku明细 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param orderCode
     * @param vwWarehouseCode
     * @return
     */
    List<OrderVwMoveDetailDTO> queryNeedOrderVmMoveInfo(String orderCode, String vwWarehouseCode);

    /**
     * @Description: 保存预约单虚仓调拨信息 <br>
     *
     * @Author chuwenchao 2020/4/9
     * @param orderVwMoveDTO
     * @return 
     */
    void saveNeedOrderVmMoveInfo(OrderVwMoveDTO orderVwMoveDTO);
}
