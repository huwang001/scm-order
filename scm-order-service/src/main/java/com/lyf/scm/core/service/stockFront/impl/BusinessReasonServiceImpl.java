package com.lyf.scm.core.service.stockFront.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lyf.scm.core.api.dto.stockFront.BusinessReasonDTO;
import com.lyf.scm.core.domain.convert.stockFront.BusinessReasonConvertor;
import com.lyf.scm.core.mapper.stockFront.BusinessReasonMapper;
import com.lyf.scm.core.service.stockFront.BusinessReasonService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("businessReasonService")
public class BusinessReasonServiceImpl implements BusinessReasonService {

	@Resource
	private BusinessReasonMapper businessReasonMapper;

	@Resource
	private BusinessReasonConvertor businessReasonConvertor;

	/**
	 * 根据单据类型查询业务原因列表
	 */
	@Override
	public List<BusinessReasonDTO> queryBusinessReasonByRecordType(Integer recordType) {
		return businessReasonConvertor
				.convertEList2DTOList(businessReasonMapper.queryBusinessReasonByRecordType(recordType));
	}

}