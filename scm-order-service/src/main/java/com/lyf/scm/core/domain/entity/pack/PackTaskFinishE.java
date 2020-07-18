package com.lyf.scm.core.domain.entity.pack;

import com.lyf.scm.core.domain.model.pack.PackTaskFinishDO;
import lombok.Data;

import java.util.List;

@Data
public class PackTaskFinishE extends PackTaskFinishDO {

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 任务完成清单明细
     */
    private List<PackTaskFinishDetailE> operationDetails;
}
