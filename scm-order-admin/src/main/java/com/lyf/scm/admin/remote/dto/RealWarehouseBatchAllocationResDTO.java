package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode
public class RealWarehouseBatchAllocationResDTO implements Serializable {

    @ApiModelProperty(value = "成功调拨预约单号")
    private List<String> successList;

    @ApiModelProperty(value = "失败调拨预约单号")
    private List<String> failList;

}
