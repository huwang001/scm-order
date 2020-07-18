package com.lyf.scm.core.remote.stock.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class RealWarehouseAllocation implements Serializable {

    @ApiModelProperty(value = "预约单号")
    private String orderCode;
    @ApiModelProperty(value = "仓库编码")
    private String allotRealWarehouseCode;

    public RealWarehouseAllocation(String orderCode, String allotRealWarehouseCode) {
        this.orderCode = orderCode;
        this.allotRealWarehouseCode = allotRealWarehouseCode;
    }
}
