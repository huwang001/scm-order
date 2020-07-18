package com.lyf.scm.core.mapper.common;

import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RecordStatusLogMapper {
	
	/**
	 * 保存单据状态流转日志
	 * 
	 * @param recordStatusLogE
	 * @return
	 */
    int insertRecordStatusLog(RecordStatusLogE recordStatusLogE);
    
    /**
     * 批量保存单据状态流转日志
     * 
     * @param recordStatusLogEList
     * @return
     */
    int batchInsertRecordStatusLog(List<RecordStatusLogE> recordStatusLogEList);

    /**
     * 根据预约单号查询单据状态流转日志列表
     * 
     * @param orderCode
     * @return
     */
	List<RecordStatusLogE> queryRecordStatusLogByOrderCode(@Param("orderCode") String orderCode);


}