package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.SalesReturnDO;
import lombok.Data;

import java.util.List;

@Data
public class SalesReturnE extends SalesReturnDO {

    /**
     * 门店零售退货明细
     */
    private List<SalesReturnDetailE> frontRecordDetails;
}
