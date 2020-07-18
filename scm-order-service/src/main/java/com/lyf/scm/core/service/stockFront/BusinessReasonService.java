package com.lyf.scm.core.service.stockFront;

import java.util.List;

import com.lyf.scm.core.api.dto.stockFront.BusinessReasonDTO;

public interface BusinessReasonService {

	/**
	 * 根据单据类型查询业务原因列表
	 * 
	 * @param recordType
	 * @return
	 */
	List<BusinessReasonDTO> queryBusinessReasonByRecordType(Integer recordType);

}