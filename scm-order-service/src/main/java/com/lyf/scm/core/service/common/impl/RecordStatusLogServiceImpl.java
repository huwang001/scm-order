package com.lyf.scm.core.service.common.impl;

import com.lyf.scm.core.api.dto.common.RecordStatusLogDTO;
import com.lyf.scm.core.domain.convert.common.RecordStatusLogConvert;
import com.lyf.scm.core.domain.entity.common.RecordStatusLogE;
import com.lyf.scm.core.mapper.common.RecordStatusLogMapper;
import com.lyf.scm.core.service.common.RecordStatusLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: RecordStatusLogService
 * <p>
 * @Author: chuwenchao  2020/2/21
 */
@Slf4j
@Service("recordStatusLogService")
public class RecordStatusLogServiceImpl implements RecordStatusLogService {

    @Resource
    private RecordStatusLogMapper recordStatusLogMapper;
    
    @Resource
    private RecordStatusLogConvert recordStatusLogConvert;

    /**
     * @Description: 通过预约单号查询状态记录 <br>
     *
     * @Author chuwenchao 2020/3/9
     * @param orderCode
     * @return
     */
    @Override
    public List<RecordStatusLogDTO> queryRecordStatusLogByOrderCode(String orderCode) {
    	List<RecordStatusLogE> RecordStatusLogEList = recordStatusLogMapper.queryRecordStatusLogByOrderCode(orderCode);
        return recordStatusLogConvert.convertEList2DTOList(RecordStatusLogEList);
    }

    /**
     * @Description: 保存单据状态流转日志 <br>
     *
     * @Author chuwenchao 2020/3/11
     * @param statusLogE
     * @return
     */
    @Override
    public Integer insertRecordStatusLog(RecordStatusLogE statusLogE) {
        return recordStatusLogMapper.insertRecordStatusLog(statusLogE);
    }

}