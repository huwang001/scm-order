package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 电商，旺店通下单入参
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
public class OnlineOrderDTO implements Serializable {

    @ApiModelProperty(value = "渠道编码", required = true)
    @NotBlank
    private String channelCode;
    @ApiModelProperty(value = "指定仓库寻源")
    private Long realWarehouseId;
    @ApiModelProperty(value = "原始订单号", required = true)
    private String originOrderCode;
    @ApiModelProperty(value = "支付时间", required = true)
    private Date payTime;
    @NotBlank
    @ApiModelProperty(value = "外部单号(SO单号)", required = true)
    private String orderCode;
    @ApiModelProperty(value = "商户id", required = true)
    @NotNull
    private Long merchantId;
    @ApiModelProperty(value = "收货人手机号", required = true)
    private String mobile;
    @ApiModelProperty(value = "省", required = true)
    private String province;
    @ApiModelProperty(value = "省编码", required = true)
    private String provinceCode;
    @ApiModelProperty(value = "市", required = true)
    private String city;
    @ApiModelProperty(value = "市编码", required = true)
    private String cityCode;
    @ApiModelProperty(value = "区县", required = true)
    private String county;
    @ApiModelProperty(value = "区县编码", required = true)
    private String countyCode;
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;
    @ApiModelProperty(value = "收货人姓名", required = true)
    private String name;
    @ApiModelProperty(value = "外部系统数据创建时间(下单时间)", required = true)
    @NotNull
    private Date outCreateTime;
    @ApiModelProperty(value = "收货地址邮编")
    private String postcode;
    @ApiModelProperty(value = "用户code")
    private String userCode;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "收货人邮箱")
    private String email;
    @ApiModelProperty(value = "证件类型")
    private String idType;
    @ApiModelProperty(value = "证件号码")
    private String idNumber;
    @ApiModelProperty(value = "交易类型:1-普通,2-预售,3-拼团,4-拼券,5-旺店通,6-POS门店,7-外卖自营,8-外卖第三方,9-电商超市,10-2B分销,11-加盟商 12-虚拟商品下单")
    private Integer transType;
    @ApiModelProperty(value = "期望收货日期_开始")
    private Date expectReceiveDateStart;
    @ApiModelProperty(value = "期望收货日期_结束")
    private Date expectReceiveDateEnd;
    @ApiModelProperty(value = "sku数量及明细")
    @Valid
    @NotNull
    private List<OnlineOrderDetailDTO> frontRecordDetails;

}
