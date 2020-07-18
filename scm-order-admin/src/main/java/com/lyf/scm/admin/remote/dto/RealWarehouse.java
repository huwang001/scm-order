package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
public class RealWarehouse implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "仓库编码")
    private String realWarehouseCode;
    @ApiModelProperty(value = "仓库外部编号-wms")
    private String realWarehouseOutCode;
    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;
    @ApiModelProperty(value = "工厂名称")
    private String factoryName;
    @ApiModelProperty(value = "仓库名称")
    private String realWarehouseName;
    @ApiModelProperty(value = "仓库类型")
    private Integer realWarehouseType;
    @ApiModelProperty(value = "门店编号")
    private String shopCode;
    @ApiModelProperty(value = "仓库状态")
    private Integer realWarehouseStatus;
    @ApiModelProperty(value = "优先级")
    private Integer realWarehousePriority;
    @ApiModelProperty(value = "邮编")
    private String realWarehousePostcode;
    @ApiModelProperty(value = "联系手机")
    private String realWarehouseMobile;
    @ApiModelProperty(value = "联系电话")
    private String realWarehousePhone;
    @ApiModelProperty(value = "联系邮箱")
    private String realWarehouseEmail;
    @ApiModelProperty(value = "国家")
    private String realWarehouseCountry;
    @ApiModelProperty(value = "省份")
    private String realWarehouseProvince;
    @ApiModelProperty(value = "城市")
    private String realWarehouseCity;
    @ApiModelProperty(value = "区县")
    private String realWarehouseCounty;
    @ApiModelProperty(value = "四级区域")
    private String realWarehouseArea;
    @ApiModelProperty(value = "创建日期")
    private String createDate;
    @ApiModelProperty(value = "国家code")
    private String realWarehouseCountryCode;
    @ApiModelProperty(value = "省份code")
    private String realWarehouseProvinceCode;
    @ApiModelProperty(value = "城市code")
    private String realWarehouseCityCode;
    @ApiModelProperty(value = "县城市code")
    private String realWarehouseCountyCode;
    @ApiModelProperty(value = "四级区域code")
    private String realWarehouseAreaCode;
    @ApiModelProperty(value = "详细地址")
    private String realWarehouseAddress;
    @ApiModelProperty(value = "联系人姓名")
    private String realWarehouseContactName;
    @ApiModelProperty(value = "备注信息")
    private String realWarehouseRemark;
    @ApiModelProperty(value = "操作人用户id", required = true)
    private Long userId;
    @ApiModelProperty(value = "是否归中台管控0否1是")
    private Integer hasConfig;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建人")
    private Long creator;
    @ApiModelProperty(value = "修改人")
    private Long modifier;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
