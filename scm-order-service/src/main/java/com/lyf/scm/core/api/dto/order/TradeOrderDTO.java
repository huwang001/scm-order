package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 接收交易中心预约单对象
 */
@Data
@ApiModel(value = "预约单对象")
public class TradeOrderDTO {

    @ApiModelProperty(value = "单据类型 1:普通订单 2:卡券订单")
    @NotNull(message = "单据类型必须为1普通订单或者2卡卷订单")
    private Integer orderType;

    @ApiModelProperty(value = "渠道编号")
    @NotBlank(message="渠道编号不能为空")
    private String channelCode;

    @ApiModelProperty(value = "销售单号")
    @NotBlank(message="销售单号不能为空")
    private String saleCode;

    @ApiModelProperty(value = "客户名称")
    @NotBlank(message="客户名称不能为空")
    private String customName;

    @ApiModelProperty(value = "客户手机号")
    @NotBlank(message="客户手机号不能为空")
    private String customMobile;

    @ApiModelProperty(value = "省编号")
    @NotBlank(message="省编号不能为空")
    private String provinceCode;

    @ApiModelProperty(value = "省名称")
    @NotBlank(message="省名称不能为空")
    private String provinceName;

    @ApiModelProperty(value = "市编号")
    @NotBlank(message="市编号不能为空")
    private String cityCode;

    @ApiModelProperty(value = "市名称")
    @NotBlank(message="市名称不能为空")
    private String cityName;

    @ApiModelProperty(value = "区编号")
    @NotBlank(message="区编号不能为空")
    private String countyCode;

    @ApiModelProperty(value = "区名称")
    @NotBlank(message="区名称不能为空")
    private String countyName;

    @ApiModelProperty(value = "客户详细地址")
    @NotBlank(message="客户详细地址不能为空")
    private String customAddress;

    @ApiModelProperty(value = "是否需要包装 0不需要 1需要")
    @NotNull(message = "是否需要包装必须为0不需要包装或者1需要包装")
    private Integer needPackage;

    @ApiModelProperty("包装类型 1:组合 2:组装")
    private Integer packageType;

    @ApiModelProperty(value = "预计交货日期")
    @NotNull(message = "预计交货日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expectDate;
    
    @ApiModelProperty(value = "包装份数")
    private Integer packageNum;
    
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "sku明细")
    @Valid
    @NotEmpty
    private List<TradeOrderDetailDTO> orderDetails;

    @Data
    public static class TradeOrderDetailDTO {
        @ApiModelProperty(value = "商品编号")
        @NotBlank(message = "商品编号不能为空")
        private String skuCode;

        @ApiModelProperty(value = "下单数量")
        @DecimalMin(value = "0", message = "下单数量不能为0")
        private BigDecimal orderQty;

        @ApiModelProperty(value = "单位编号")
        @NotBlank(message = "单位编号不能为空")
        private String unitCode;

        @ApiModelProperty(value = "单位名称")
        @NotBlank(message = "单位名称不能为空")
        private String unit;
    }
}
