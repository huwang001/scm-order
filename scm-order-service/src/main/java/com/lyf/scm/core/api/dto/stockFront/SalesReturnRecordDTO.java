package com.lyf.scm.core.api.dto.stockFront;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * TOB前置单据
 */
@Data
@EqualsAndHashCode
public class SalesReturnRecordDTO {

    @ApiModelProperty(value = "单据编码")
    @NotBlank(message = "单据编码不能为空")
    private String outRecordCode;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "店铺编码")
    @NotBlank(message = "店铺编码不能为空")
    private String shopCode;

    @NotNull(message = "商家id不能为空")
    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @ApiModelProperty(value = "用户手机")
    @Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
    private String mobile;

    @ApiModelProperty(value = "退货时间")
    @NotNull(message = "退货时间不能为空")
    private Date outCreateTime;

    @ApiModelProperty(value = "退货原因")
    private String reason;

    @ApiModelProperty(value = "买家账户")
    private String userCode;

    @JsonIgnore
    private String recordCode;

    @JsonIgnore
    private int recordType;

    @Valid
    @ApiModelProperty(value = "sku数量及明细")
    @NotNull(message = "sku数量及明细不能为空")
    private List<SalesReturnDetailDTO> frontRecordDetails;
}
