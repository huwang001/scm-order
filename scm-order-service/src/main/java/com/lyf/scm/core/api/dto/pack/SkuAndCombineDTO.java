package com.lyf.scm.core.api.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zys
 * @Remark
 * @date 2020/7/11
 */
@Data
public class SkuAndCombineDTO {

    @ApiModelProperty(value = "包装需求单成品明细列表")
    private List<PackDemandDetailDTO> packDemandDetailDTOList;

    @ApiModelProperty(value = "组件单明细列表")
    private List<PackDemandComponentDTO> packDemandComponentDTOList;
}
