package com.lyf.scm.core.api.dto.online;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 电商，旺店通修改修改so地址入参
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
public class OnlineOrderRecordDTO implements Serializable {

    @ApiModelProperty(value = "SO单号", required = true)
    private String orderCode;
    @ApiModelProperty(value = "收货人手机号")
    private String mobile;
    @ApiModelProperty(value = "收货人姓名")
    private String name;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "收货地址邮编")
    private String postcode;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "收货人邮箱")
    private String email;


    //省市区
    @ApiModelProperty(value = "省份")
    private String province;
    @ApiModelProperty(value = "省份code")
    private String provinceCode;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "城市code")
    private String cityCode;
    @ApiModelProperty(value = "四级区域")
    private String area;
    @ApiModelProperty(value = "四级区域code")
    private String areaCode;

}
