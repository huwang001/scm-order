package com.lyf.scm.core.domain.entity.pack;

import com.lyf.scm.core.domain.model.pack.PackDemandDetailDO;
import lombok.Data;

import java.util.List;

@Data
public class PackDemandDetailE extends PackDemandDetailDO {


    /**
     * 组件明细
     */
    private List<PackDemandComponentE>  componentEList;
}
