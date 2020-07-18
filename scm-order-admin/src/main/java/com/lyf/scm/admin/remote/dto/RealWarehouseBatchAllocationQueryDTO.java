package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class RealWarehouseBatchAllocationQueryDTO implements Serializable {

    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;
    @ApiModelProperty(value = "是否需要包装 0:不需要 1:需要")
    private Long needPackage;

}
