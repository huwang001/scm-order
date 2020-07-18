package com.lyf.scm.core.domain.entity.online;

import com.lyf.scm.core.domain.model.online.RecordPoolDO;
import lombok.Data;

import java.util.List;

@Data
public class RecordPoolE extends RecordPoolDO {

    /**
     * 修改前单据id(数据库无此字段)
     */
    private Long beforeRecordId;

    /**
     * 单据池详情
     */
    private List<RecordPoolDetailE> rwRecordPoolDetails;
}
