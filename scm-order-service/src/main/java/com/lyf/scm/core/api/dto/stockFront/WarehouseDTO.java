package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.remote.base.dto.ChannelDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Doc:创建仓库入参DTO
 * @Author: lchy
 * @Date: 2019/7/31
 * @Version 1.0
 */
@Data
public class WarehouseDTO implements Serializable {
    @NotNull
    @ApiModelProperty(value = "商家ID", required = true)
    private Long merchantId;
    @NotBlank
    @ApiModelProperty(value = "仓库名称", required = true)
    private String warehouseName;
    @NotNull
    @ApiModelProperty(value = "仓库类型 1商家仓 2虚拟仓 3门店仓", required = true)
    private Integer warehouseType;
    @ApiModelProperty(value = "工厂编码")
    private String factoryCode;
    @ApiModelProperty(value = "仓库外部编码")
    private String warehouseOutCode;
    @ApiModelProperty(value = "门店编码")
    private String shopCode;
    @Valid
    @NotNull
    @ApiModelProperty(value = "渠道信息", required = true)
    private List<ChannelDTO> channelDTOList;
    @ApiModelProperty(value = "国家编码")
    private String countryCode;
    @ApiModelProperty(value = "省编码")
    private String provinceCode;
    @ApiModelProperty(value = "城市编码")
    private String cityCode;
    @ApiModelProperty(value = "仓库详细地址")
    private String realWarehouseAddress;
    @ApiModelProperty(value = "联系人手机号")
    private String realWarehouseMobile;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "联系人")
    private String realWarehouseContactName;
}
