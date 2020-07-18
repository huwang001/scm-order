package com.lyf.scm.core.service.common;

import com.lyf.scm.core.api.dto.common.RecordStatusLogDTO;
import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;

import java.util.List;


/**
 * @Description: 单据状态流转相关Service <br>
 *
 * @Author chuwenchao 2020/3/2
 */
public interface RecordStatusLogService {

    /**
     * @Description: 通过需求单号查询状态记录 <br>
     *
     * @Author chuwenchao 2020/3/9
     * @param orderCode
     * @return 
     */
    List<RecordStatusLogDTO> queryRecordStatusLogByOrderCode(String orderCode);

	/**
	 * @Description: 保存单据状态流转日志 <br>
	 *
	 * @Author chuwenchao 2020/3/11
	 * @param statusLogE
	 * @return 
	 */
	Integer insertRecordStatusLog(RecordStatusLogE statusLogE);
}
