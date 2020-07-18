package com.lyf.scm.admin.remote.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.order.OrderVwMoveDTO;
import com.lyf.scm.admin.dto.order.OrderVwMoveDetailDTO;
import com.lyf.scm.admin.remote.OrderVwMoverRemoteService;
import com.lyf.scm.admin.remote.dto.VirtualWarehouse;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class OrderVwMoverFacade {

    @Resource
    private OrderVwMoverRemoteService orderVwMoverRemoteService;

    /**
     * 通过工厂编码和仓库编码查询虚仓列表
     * @param factoryCode
     * @param realWarehouseOutCode
     * @return
     */
    public List<VirtualWarehouse> queryVmListByCodes(String factoryCode,String realWarehouseOutCode) {
        Response<List<VirtualWarehouse>> response = orderVwMoverRemoteService.queryVmListByCodes(factoryCode,realWarehouseOutCode);
        if ("0".equals(response.getCode())) {
            return response.getData();
        } else {
            log.error(response.getCode() + ":" + response.getMsg());
            return null;
        }
    }

    /**
     * 查询预约单待虚仓调拨Sku明细
     * @param orderCode
     * @return
     */
    public PageInfo<OrderVwMoveDetailDTO> queryNeedOrderVmMoveInfo(String orderCode, String vwWarehouseCode, Integer pageNum,Integer pageSize) {
        Response<PageInfo<OrderVwMoveDetailDTO>> response = orderVwMoverRemoteService.queryNeedOrderVmMoveInfo(orderCode,vwWarehouseCode,pageNum,pageSize);
        if ("0".equals(response.getCode())) {
            return response.getData();
        } else {
            log.error(response.getCode() + ":" + response.getMsg());
            return null;
        }
    }

    /**
     * 保存预约单虚仓调拨信息
     * @param orderVwMoveDTO
     * @return
     */
    public boolean saveNeedOrderVmMoveInfo(OrderVwMoveDTO orderVwMoveDTO) {
        Response response = orderVwMoverRemoteService.saveNeedOrderVmMoveInfo(orderVwMoveDTO);
        if ("0".equals(response.getCode())) {
            return true;
        } else {
            log.error(response.getCode() + ":" + response.getMsg());
            return false;
        }
    }

    /**
     * 查询虚仓信息
     * @param virtualWarehouseCodes
     * @return
     */
    public List<VirtualWarehouse> queryVirtualWarehouseByCodes(List<String> virtualWarehouseCodes) {
        Response<List<VirtualWarehouse>> response = orderVwMoverRemoteService.queryVirtualWarehouseByCodes(virtualWarehouseCodes);
        if ("0".equals(response.getCode())) {
            return response.getData();
        } else {
            log.error(response.getCode() + ":" + response.getMsg());
            return null;
        }
    }
}
