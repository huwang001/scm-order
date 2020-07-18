package com.lyf.scm.core.service.online;

import com.lyf.scm.core.api.dto.online.RecordPoolDTO;

import java.util.List;

/**
 * @Remarks
 * @date 2020/7/2
 */
public interface OnlineOrderQueryService {

    /**
     * 根据单据池DO单号查询单据池信息
     */
    List<RecordPoolDTO> queryPoolRecordByPoolRecordCode(List<String> poolRecordCodeList);

    /**
     * 根据父doCode查询子do列表
     * @param parentCode
     * @return
     */
    List<String> queryChildDoCodeByParentDo(String parentCode);
}
