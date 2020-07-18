package com.lyf.scm.core.api.dto.stockFront;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description 调拨前置单据
 * @date 2020/6/15
 * @Version
 */
@Data
@EqualsAndHashCode
public class ShopAllocationRecordDTO extends BaseDTO implements Serializable {
    @ApiModelProperty(value = "单据编码")
    @NotBlank(message="单据编码不能为空")
    private String outRecordCode;

    @JsonIgnore
    private Long channelId;

    @ApiModelProperty(value = "调出店铺编码")
    @NotBlank(message="店铺编码不能为空")
    private String outShopCode;

    @ApiModelProperty(value = "调入店铺编码")
    @NotBlank(message="店铺编码不能为空")
    private String inShopCode;

    @JsonIgnore
    private String realWarehouseId;

    @JsonIgnore
    private String recordStatusReason;

    @ApiModelProperty(value = "调拨备注")
    private String remark;

    @ApiModelProperty(value = "调拨时间")
    @NotNull(message="调拨时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date outCreateTime;

    @ApiModelProperty(value = "调拨单号")
    private String recordCode;

    @JsonIgnore
    private Integer recordType;

    @ApiModelProperty(value = "记账用户")
    private String userName;

    @ApiModelProperty(value = "sku数量及明细")
    @NotNull(message="sku数量及明细不能为空")
    @Valid
    private List<ShopAllocationDetailDTO> frontRecordDetails;
}
