package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 接收交易中心退货单传输对象
 * <p>
 * @Author: wwh 2020/4/9
 */
@Data
public class ReturnDTO implements Serializable {
	
	@ApiModelProperty(value = "销售单号")
	@NotBlank(message="销售单号不能为空")
    private String saleCode;

	@ApiModelProperty(value = "售后单号")
	@NotBlank(message="售后单号不能为空")
    private String afterSaleCode;

	@ApiModelProperty(value = "客户名称")
	@NotBlank(message="客户名称不能为空")
    private String customName;

	@ApiModelProperty(value = "客户手机号")
	@NotBlank(message="客户手机号不能为空")
    private String customMobile;

    @ApiModelProperty(value = "省Code")
    @NotBlank(message="省Code不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "省名称")
    @NotBlank(message="省名称不能为空")
    private String provinceName;

    @ApiModelProperty(value = "市code")
    @NotBlank(message="市code不能为空")
    private String cityCode;

    @ApiModelProperty(value = "市名称")
    @NotBlank(message="市名称不能为空")
    private String cityName;

    @ApiModelProperty(value = "区code")
    @NotBlank(message="区code不能为空")
    private String countyCode;

    @ApiModelProperty(value = "区名称")
    @NotBlank(message="区名称不能为空")
    private String countyName;

    @ApiModelProperty(value = "客户详细地址")
    @NotBlank(message="客户详细地址不能为空")
    private String customAddress;

    @ApiModelProperty(value = "退货原因")
    @NotBlank(message="退货原因不能为空")
    private String reason;

    @ApiModelProperty(value = "快递单号")
    @NotBlank(message="快递单号不能为空")
    private String expressNo;
    
    @ApiModelProperty(value = "退货单详情")
    @NotNull(message="退货单详情不能为空")
    @Valid
    private List<ReturnDetailDTO> returnDetailList;
    
}