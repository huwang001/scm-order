package com.lyf.scm.core.mapper.pack;

import com.lyf.scm.core.domain.entity.pack.PackTaskFinishDetailE;

import java.util.List;

public interface PackTaskFinishDetailMapper {

    /**
     * 保存完成操作单 明细
     *
     * @param detailEList
     */
    void saveTaskFinishDetail(List<PackTaskFinishDetailE> detailEList);

    /**
     * 批量根据 任务完成单编码 查询 明细
     *
     * @param recordCodeList
     * @return
     */
    List<PackTaskFinishDetailE> queryFinishDetailListByFrontRecordCodeList(List<String> recordCodeList);
}