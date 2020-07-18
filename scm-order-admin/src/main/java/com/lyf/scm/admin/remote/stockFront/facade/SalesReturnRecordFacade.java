package com.lyf.scm.admin.remote.stockFront.facade;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.admin.remote.stockFront.SalesReturnRecordRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.SalesReturnRecordParamDTO;
import com.lyf.scm.admin.remote.stockFront.dto.SalesReturnWarehouseRecordDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SalesReturnRecordFacade {

    @Resource
    private SalesReturnRecordRemoteService salesReturnRecordRemoteService;

    public PageInfo<SalesReturnWarehouseRecordDTO> findBySalesReturnRecordCondition(SalesReturnRecordParamDTO salesReturnRecordParamDTO) {
        if (StringUtils.isNotBlank(salesReturnRecordParamDTO.getChannelCodes())) {
            List<String> channelCodeList = Arrays.asList(salesReturnRecordParamDTO.getChannelCodes().split(","));
            salesReturnRecordParamDTO.setChannelCodeList(channelCodeList);
        }
        Response<PageInfo<SalesReturnWarehouseRecordDTO>> response = salesReturnRecordRemoteService.findBySalesReturnRecordCondition(salesReturnRecordParamDTO);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    public SalesReturnWarehouseRecordDTO querySaleReturnWarehouseRecordInfoById(Long recordId) {
        Response<SalesReturnWarehouseRecordDTO> response = salesReturnRecordRemoteService.querySaleReturnWarehouseRecordInfoById(recordId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }
}
