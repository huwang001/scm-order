package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.common.model.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 类ShopReplenishDTO的实现描述：门店补货
 *
 * @author sunyj 2019/4/21 21:03
 */
@Data
@EqualsAndHashCode
public class ShopReplenishDTO extends Pagination {

    @ApiModelProperty(value = "外部系统单据编码,对应PO单号", required = true)
    @NotBlank(message="单据编码不能为空")
    private String outRecordCode;

    @ApiModelProperty(value = "渠道编码", required = true)
    private String channelCode;

    @ApiModelProperty(value = "公司渠道编码", required = true)
    private String parentChannelCode;

    @ApiModelProperty(value = "门店Code", required = true)
    @NotNull(message="门店Code不能为空")
    private String shopCode;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotBlank(message="门店名称不能为空")
    private String shopName;


    @ApiModelProperty(value = "仓库code,仅当类型为指定仓库配送是必传")
    private String warehouseCode;

    @ApiModelProperty(value = "工厂code,仅当类型为指定仓库配送是必传")
    private String factoryCode;


    @ApiModelProperty(value = "门店性质：1 直营,3 加盟,5 加盟托管")
    @NotNull(message="门店类型不能为空")
    private Integer shopType;


    @ApiModelProperty(value = "配货类型 1普通配货 2指定仓库配货",required = true)
    @NotNull(message="配货类型不能为空")
    private Integer requireType;

    @ApiModelProperty(value = "采购单类型 1直营普通采购 2加盟采购 3供应商直送  4冷链 5加盟主配", required = true)
    @NotNull(message="采购单类型不能为空")
    private Integer recordType;

    @ApiModelProperty(value = "要货时间,对用PO的expectDate")
    @NotNull(message="要货时间不能为空")
    private Date replenishTime;

    @ApiModelProperty(value = "外部系统数据创建时间")
    @NotNull(message="创建时间不能为空")
    private Date outCreateTime;

    @ApiModelProperty(value = "SAP的po单号")
    private String sapPoNo;

    @Valid
    @ApiModelProperty(value = "sku数量及明细")
    @NotNull(message="sku数量及明细不能为空")
    private List<ShopReplenishDetailDTO> frontRecordDetails;

    @ApiModelProperty(value = "是否需要分配(0: 无需分配, 1.需要分配 2. 分配完成)")
    private Integer isNeedAllot;
}
