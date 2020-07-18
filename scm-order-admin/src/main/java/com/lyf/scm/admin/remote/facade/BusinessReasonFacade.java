package com.lyf.scm.admin.remote.facade;

import com.lyf.scm.admin.remote.BusinessReasonRemoteService;
import com.lyf.scm.admin.remote.stockFront.dto.BusinessReasonDTO;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class BusinessReasonFacade {

    @Resource
    BusinessReasonRemoteService businessReasonRemoteService;

    /**
     * 根据单据类型查询业务原因字典
     *
     * @param recordType
     * @return
     */
    public List<BusinessReasonDTO> queryBusinessReason(Integer recordType) {
        Response<List<BusinessReasonDTO>> rep = businessReasonRemoteService.queryBusinessReason(recordType);
        ResponseValidateUtils.validResponse(rep);
        return rep.getData();
    }
}
