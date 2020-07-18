package com.lyf.scm.core.remote.trade.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel(description = "门店退货单-交易中心更新单")
@Data
@EqualsAndHashCode
public class UpdateReversePoDTO {

    @ApiModelProperty(value = "发货单号")
    @NotBlank(message = "发货单号不能为空")
    private String doNo;

    @ApiModelProperty(value = "退货单明细集合")
    private List<UpdateReversePoLineDTO> reversePoLineQuantityDTOs;
}
