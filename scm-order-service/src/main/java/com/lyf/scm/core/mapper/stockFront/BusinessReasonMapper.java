package com.lyf.scm.core.mapper.stockFront;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lyf.scm.core.domain.entity.stockFront.BusinessReasonE;

/**
 * 业务原因数据库映射接口
 */
@Mapper
public interface BusinessReasonMapper {

	/**
	 * 根据单据类型查询业务原因列表
	 * 
	 * @param recordType
	 * @return
	 */
	List<BusinessReasonE> queryBusinessReasonByRecordType(Integer recordType);

}