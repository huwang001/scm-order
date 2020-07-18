package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.dto.SaleWarehouseRecordCondition;
import com.lyf.scm.admin.remote.stockFront.SaleWarehouseRecordRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.SaleWarehouseRecordDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class SaleWarehouseRecordFacade {

    @Resource
    private SaleWarehouseRecordRemoteService saleWarehouseRecordRemoteService;

    public PageInfo<SaleWarehouseRecordDTO> queryByCondition(SaleWarehouseRecordCondition condition) {
        if (StringUtils.isNotBlank(condition.getChannelCodes())) {
            List<String> channelCodeList = Arrays.asList(condition.getChannelCodes().split(","));
            condition.setChannelCodeList(channelCodeList);
        }
        Response<PageInfo<SaleWarehouseRecordDTO>> response = saleWarehouseRecordRemoteService.queryByCondition(condition);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public SaleWarehouseRecordDTO querySaleWarehouseRecordInfoById(Long recordId) {
        Response<SaleWarehouseRecordDTO> response = saleWarehouseRecordRemoteService.querySaleWarehouseRecordInfoById(recordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
