package com.lyf.scm.admin.remote.facade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lyf.scm.admin.remote.RwAllocationRemoteService;
import com.lyf.scm.admin.remote.dto.RealWarehouse;
import com.lyf.scm.admin.remote.dto.RealWarehouseBatchAllocationDTO;
import com.lyf.scm.admin.remote.dto.RealWarehouseBatchAllocationResDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import com.rome.user.common.utils.UserContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RwAllocationFacade {

    @Resource
    private RwAllocationRemoteService rwAllocationRemoteService;


    public List<RealWarehouse> queryRealWarehouseByFactoryCodeAndType(String factoryCode,Integer type) {
        Response<List<RealWarehouse>> response = rwAllocationRemoteService.queryRealWarehouseByFactoryCodeAndType(factoryCode,type);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public boolean realWarehouseAllocation(String orderCode, String realWarehouseCode, Long userId) {
        Response response = rwAllocationRemoteService.realWarehouseAllocation(orderCode, realWarehouseCode, userId);
        ResponseValidateUtils.validResponse(response);
        return true;
    }

    public RealWarehouseBatchAllocationResDTO realWarehouseBatchAllocation(List<RealWarehouseBatchAllocationDTO> realWarehouseBatchAllocationDTOList) {
        RealWarehouseBatchAllocationResDTO res=new RealWarehouseBatchAllocationResDTO();
        List<String> successList=new ArrayList<>();
        List<String> failList=new ArrayList<>();
        Long userId = UserContext.getUserId();
        for(RealWarehouseBatchAllocationDTO realWarehouseBatchAllocationDTO:realWarehouseBatchAllocationDTOList){
            Response response = rwAllocationRemoteService.realWarehouseAllocation(realWarehouseBatchAllocationDTO.getOrderCode(),realWarehouseBatchAllocationDTO.getAllotRealWarehouseCode(), userId);
            if ("0".equals(response.getCode())) {
                successList.add(realWarehouseBatchAllocationDTO.getOrderCode());
            } else {
                log.error("批量调拨返回失败："+response.getCode() + ":" + response.getMsg());
                failList.add(realWarehouseBatchAllocationDTO.getOrderCode());
            }
        }
        res.setFailList(failList);
        res.setSuccessList(successList);
        return res;
    }
}
