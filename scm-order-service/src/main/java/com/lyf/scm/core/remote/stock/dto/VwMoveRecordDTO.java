package com.lyf.scm.core.remote.stock.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class VwMoveRecordDTO {

    /**
     * 预约单号
     */
    private String orderCode;

    /**
     * 虚拟仓库code（入）
     */
    @NotNull(message = "调入虚拟仓库code不能为空")
    private String inVirtualWarehouseCode;
    /**
     * 虚拟仓库code（出）
     */
    @ApiModelProperty(value = "虚拟仓库code（出）")
    private String outVirtualWarehouseCode;

    /**
     * 转移商品细节
     */
    @ApiModelProperty(value = "转移商品细节")
    private List<VwDetailDTO> virtualWarehouseSkus;
}
