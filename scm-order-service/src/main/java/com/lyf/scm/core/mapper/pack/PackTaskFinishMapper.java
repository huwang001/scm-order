package com.lyf.scm.core.mapper.pack;

import com.lyf.scm.core.api.dto.pack.TaskFinishPageDTO;
import com.lyf.scm.core.domain.entity.pack.PackTaskFinishE;

import java.math.BigDecimal;
import java.util.List;

public interface PackTaskFinishMapper {

    /**
     * 批量根据操作单号 查询完成操作单
     *
     * @param taskOperationCodeList
     * @return
     */
    List<PackTaskFinishE> batchQueryByTaskOperationCodes(List<String> taskOperationCodeList);

    /**
     * 保存完成操作单
     *
     * @param packTaskFinishE
     */
    void saveTaskOperationOrder(PackTaskFinishE packTaskFinishE);

    /**
     * 分页查询任务完成清单
     *
     * @param pageDTO
     * @return
     */
    List<PackTaskFinishE> queryTaskFinishPage(TaskFinishPageDTO pageDTO);
}