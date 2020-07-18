package com.lyf.scm.core.mapper.common;

import com.lyf.scm.core.domain.entity.common.CallRecordLogE;

/**
 * @Description: 回调记录日志Mapper <br>
 *
 * @Author wwh 2020/2/21
 */
public interface CallRecordLogMapper {
	
	/**
	 * 保存回调记录日志
	 * 
	 * @param callRecordLogE
	 * @return
	 */
	int insertCallRecordLog(CallRecordLogE callRecordLogE);

}