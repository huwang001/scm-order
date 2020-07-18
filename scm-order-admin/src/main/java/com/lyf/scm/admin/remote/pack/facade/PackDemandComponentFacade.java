package com.lyf.scm.admin.remote.pack.facade;

import javax.annotation.Resource;
import com.lyf.scm.admin.dto.pack.PackDemandComponentDTO;
import org.springframework.stereotype.Component;
import com.lyf.scm.admin.dto.pack.DemandAllotDTO;
import com.lyf.scm.admin.remote.pack.PackDemandComponentService;
import com.lyf.scm.common.util.validate.ResponseValidateUtils;
import com.rome.arch.core.clientobject.Response;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Component
public class PackDemandComponentFacade {

    @Resource
    private PackDemandComponentService packDemandComponentService;

    /**
     * 根据需求单明细原料创建调拨单
     * 
     * @param demandAllotDTO
     */
	public void createDemandAllot(DemandAllotDTO demandAllotDTO) {
		Response response = packDemandComponentService.createDemandAllot(demandAllotDTO);
		ResponseValidateUtils.validResponse(response);
	}

	/**
	 * 调拨时根据需求编码查询需求单明细原料列表
	 * @param recordCode
	 * @return
	 */
	public List<PackDemandComponentDTO> queryDemandComponentByRecordCodeAllot(String recordCode){
		Response<List<PackDemandComponentDTO>> response = packDemandComponentService.queryDemandComponentByRecordCodeAllot(recordCode);
		ResponseValidateUtils.validResponse(response);
		return response.getData();
	}
	
}