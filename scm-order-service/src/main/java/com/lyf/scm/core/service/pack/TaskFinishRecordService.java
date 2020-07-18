package com.lyf.scm.core.service.pack;

import com.lyf.scm.core.domain.entity.pack.PackDemandE;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;

public interface TaskFinishRecordService {

    /**
     * 保存任务完成单 前置单
     *
     * @param finishE
     * @param demandE
     */
    void saveTaskFinishRecord(PackTaskFinishE finishE, PackDemandE demandE);
}
