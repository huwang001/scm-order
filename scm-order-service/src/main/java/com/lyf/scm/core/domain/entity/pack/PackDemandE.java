package com.lyf.scm.core.domain.entity.pack;

import com.lyf.scm.core.domain.model.pack.PackDemandDO;
import com.lyf.scm.core.remote.stock.dto.RealWarehouse;

import lombok.Data;

import java.util.List;

@Data
public class PackDemandE extends PackDemandDO {

    /**
     * 成品商品明细
     */
    private List<PackDemandDetailE> finishProductDetail;

    /**
     * 组件明细
     */
    private List<PackDemandComponentE>  componentEList;
    
    /**
     * 入向实仓
     */
    RealWarehouse inRealWarehouse;
	
    /**
     * 出向实仓
     */
	RealWarehouse outRealWarehouse;
	
}
