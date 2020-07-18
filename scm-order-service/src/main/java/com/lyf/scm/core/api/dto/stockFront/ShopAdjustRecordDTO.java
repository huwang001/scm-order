package com.lyf.scm.core.api.dto.stockFront;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 门店试吃调整单
 */
@Data
@EqualsAndHashCode
public class ShopAdjustRecordDTO {

    @ApiModelProperty(value = "唯一编码")
    private Long id;

    @ApiModelProperty(value = "单据编码")
    private String outRecordCode;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "店铺编码")
    private String shopCode;

    @ApiModelProperty(value = "实仓id")
    private Long realWarehouseId;

    @ApiModelProperty(value = "实仓名称")
    private String realWarehouseName;

    @ApiModelProperty(value = "仓库外部code")
    private String realWarehouseOutCode;

    @ApiModelProperty(value = "sap过账单号")
    private String sapRecordCode;

    @ApiModelProperty(value = "归属组织名称")
    private String organizationName;

    @JsonIgnore
    private String warehouseId;

    @ApiModelProperty(value = "用户手机")
    private String mobile;

    @ApiModelProperty(value = "销售时间")
    private Date outCreateTime;

    @ApiModelProperty(value = "单据编号")
    private String recordCode;

    @ApiModelProperty(value = "单据类型")
    private Integer recordType;

    @ApiModelProperty(value = "单据创建时间")
    private Date createTime;

    @ApiModelProperty(value = "单据状态")
    private Integer recordStatus;

    @ApiModelProperty(value = "单据状态描述")
    private String recordStatusDesc;

    private Date startCrateTime;

    private Date endCreateTime;

    private Long creator;

    @ApiModelProperty(value = "业务原因")
    private Integer reason;

    @ApiModelProperty(value = "业务原因描述")
    private String reasonDesc;

    @ApiModelProperty(value = "单据类型描述")
    private String recordTypeDesc;

    @ApiModelProperty(value = "sku数量及明细")
    private List<ShopAdjustDetailDTO> frontRecordDetails;
}
