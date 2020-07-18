package com.lyf.scm.core.domain.entity.online;

import com.lyf.scm.core.domain.model.online.SaleDO;
import lombok.Data;

import java.util.List;

@Data
public class SaleE extends SaleDO {

    //sku数量及明细
    private List<SaleDetailE> frontRecordDetails;
}
