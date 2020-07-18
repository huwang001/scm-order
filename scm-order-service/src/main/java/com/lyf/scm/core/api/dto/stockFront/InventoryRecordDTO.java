package com.lyf.scm.core.api.dto.stockFront;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 盘点前置单据
 *
 * @author jl
 */
@Data
@EqualsAndHashCode
public class InventoryRecordDTO {

    @ApiModelProperty(value = "单据编码")
    @NotBlank(message = "单据编码不能为空")
    private String outRecordCode;

    @ApiModelProperty(value = "店铺编码")
    @NotBlank(message = "店铺编码不能为空")
    private String shopCode;

    @ApiModelProperty(value = "抽盘类型：0-抽盘，1-全盘,9-全盘")
    private Integer businessType;

    @JsonIgnore
    private String realWarehouseId;

    @JsonIgnore
    private String recordStatusReason;

    @ApiModelProperty(value = "盘点备注")
    private String remark;

    @ApiModelProperty(value = "盘点时间")
    @NotNull(message = "盘点时间不能为空")
    private Date outCreateTime;

    @JsonIgnore
    private String recordCode;

    @JsonIgnore
    private Integer recordType;

    @ApiModelProperty(value = "sku数量及明细")
    @NotNull(message = "sku数量及明细不能为空")
    @Valid
    private List<InventoryDetailDTO> frontRecordDetails;
}
