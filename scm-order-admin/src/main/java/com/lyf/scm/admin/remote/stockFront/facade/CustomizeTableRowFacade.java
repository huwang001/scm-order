package com.lyf.scm.admin.remote.stockFront.facade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lyf.scm.admin.remote.stockFront.CustomizeTableRowRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.CustomizeTableRowDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomizeTableRowFacade {
	
    @Resource
    private CustomizeTableRowRemoteService customizeTableRowRemoteService;

    /**
     * 根据table_code和用户获取自定义数据
     * 
     * @param tableCode
     * @param userId
     * @return
     */
    public List<CustomizeTableRowDTO> getDetailByTableCodeAndUserId(String tableCode, Long userId) {
        Response<List<CustomizeTableRowDTO>> response = customizeTableRowRemoteService.getDetailByTableCodeAndUserId(tableCode,userId);
        ResponseValidateUtils.validResponse(response);
        return response.getData();
    }

    /**
     * 根据CustomizeTableRowDTO对象集合更新某表的标题信息
     * 
     * @param customizeTableRowDTOs
     */
    public void updateDetailByDates(List<CustomizeTableRowDTO> customizeTableRowDTOs) {
        Response response = customizeTableRowRemoteService.updateDetailByDates(customizeTableRowDTOs);
        ResponseValidateUtils.validResponse(response);
    }
    
}