package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.stockFront.WarehouseRecordRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.WarehouseRecordDTO;
import com.lyf.scm.admin.remote.stockFront.dto.WarehouseRecordDetailDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class WarehouseRecordFacade {

    @Resource
    private WarehouseRecordRemoteService warehouseRecordRemoteService;

    /**
     * 查询入库单列表
     * @return
     */
    public PageInfo<WarehouseRecordDTO> queryInWarehouseRecordList(WarehouseRecordDTO warehouseRecord){
        Response<PageInfo<WarehouseRecordDTO>> response = warehouseRecordRemoteService.queryInWarehouseRecordList(warehouseRecord);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询入库单列表
     * @return
     */
    public PageInfo<WarehouseRecordDTO> queryOutWarehouseRecordList(WarehouseRecordDTO warehouseRecord){
        Response<PageInfo<WarehouseRecordDTO>> response = warehouseRecordRemoteService.queryOutWarehouseRecordList(warehouseRecord);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 查询出入库详情
     * @param warehouseRecordId
     * @return
     */
    public List<WarehouseRecordDetailDTO> queryWarehouseRecordDetailList(Long warehouseRecordId){
        Response<List<WarehouseRecordDetailDTO>> response = warehouseRecordRemoteService.queryWarehouseRecordDetailList(warehouseRecordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
