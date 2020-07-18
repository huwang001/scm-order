package com.lyf.scm.core.service.pack;

import com.github.pagehelper.PageInfo;
import com.lyf.scm.core.api.dto.pack.OperationReturnDTO;
import com.lyf.scm.core.api.dto.pack.PackTaskOperationDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.core.api.dto.pack.TaskFinishResponseDTO;

import java.util.List;

public interface PackTaskOperationService {

    /**
     * 包装系统任务完成清单创建(批量)
     *
     * @param operationList
     * @return
     */
    List<OperationReturnDTO> createPackTaskOperationOrder(List<PackTaskOperationDTO> operationList);

    /**
     * 分页查询包装任务完成清单
     *
     * @param finishPage
     * @return
     */
    PageInfo<TaskFinishResponseDTO> queryPackTaskFinishRecord(TaskFinishPageDTO finishPage);
}
