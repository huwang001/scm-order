package com.lyf.scm.admin.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/8
 */
@Data
public class DemandDetailAndComponentDTO {

    @ApiModelProperty(value = "包装需求单成品明细列表")
    private List<PackDemandDetailDTO> packDemandDetailDTOList;

    @ApiModelProperty(value = "组件单明细列表")
    private List<PackDemandComponentDTO> packDemandComponentDTOList;
}
