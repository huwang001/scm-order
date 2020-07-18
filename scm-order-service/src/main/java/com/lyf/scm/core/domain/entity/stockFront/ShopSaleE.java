package com.lyf.scm.core.domain.entity.stockFront;

import com.lyf.scm.core.domain.model.stockFront.ShopSaleDO;
import lombok.Data;

import java.util.List;

@Data
public class ShopSaleE extends ShopSaleDO {

    private String warehouseId;

    private List<ShopSaleDetailE> frontRecordDetails;
}
