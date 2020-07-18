package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
public class RealWarehouseBatchAllocationDTO implements Serializable {

    @ApiModelProperty(value = "调拨仓库编码")
    private String allotRealWarehouseCode;
    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;
    @ApiModelProperty(value = "预约单号")
    private String orderCode;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    
}
