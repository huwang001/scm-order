package com.lyf.scm.core.api.dto.shopReturn;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/14
 */
@Data
public class ShopReturnDTO {

    @ApiModelProperty(value = "退货单号")
    private String recordCode;

    @ApiModelProperty(value = "单据编码")
    @NotBlank(message = "单据编码不能为空")
    private String outRecordCode;

    @ApiModelProperty(value = "店铺编码")
    @NotBlank(message = "店铺编码不能为空")
    private String shopCode;

    @ApiModelProperty(value = "门店名称")
    @NotBlank(message = "门店名称不能为空")
    private String shopName;

    @ApiModelProperty(value = "门店类型:门店类型：1加盟，2直营，3加盟联营")
    @NotNull(message = "门店类型不能为空")
    private Integer shopType;

    @ApiModelProperty(value = "退货类型 1直营退货 2加盟退货  5加盟商退货", required = true)
    @NotNull(message = "退货类型不能为空")
    private Integer recordType;

    @ApiModelProperty(value = "退货时间")
    @NotNull(message = "退货时间不能为空")
    private Date outCreateTime;

    @ApiModelProperty(value = "入向仓库Id")
    private Long inRealWarehouseId;

    @ApiModelProperty(value = "入向工厂编码")
    private String inFactoryCode;

    @ApiModelProperty(value = "入向仓库外部编码")
    private String inRealWarehouseCode;

    @ApiModelProperty(value = "出向仓库外部编码")
    private String outRealWarehouseCode;

    @ApiModelProperty(value = "出向工厂编码")
    private String outFactoryCode;

    @ApiModelProperty(value = "出向仓库Id")
    private Long outRealWarehouseId;

    @ApiModelProperty(value = "POS退货信息")
    private String posReturnInfo;

    @ApiModelProperty(value = "请求号")
    private String requestNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "期望退货日期")
    private Date expectDate;

    @ApiModelProperty(value = "SAP的退货单号")
    @NotBlank(message = "SAP的退货单号不能为空")
    private String sapReverseNo;

    @ApiModelProperty(value = "cmp退货单号")
    @NotBlank(message = "cmp退货单号不能为空")
    private String cmpRecordCode;

    @ApiModelProperty(value = "是否指定收货仓：0-否，1-是")
    private Integer isAppoint;

    @ApiModelProperty(value = "sku数量及明细")
    @NotNull(message = "sku数量及明细不能为空")
    @Valid
    private List<ShopReturnDetailDTO> frontRecordDetails;
}
