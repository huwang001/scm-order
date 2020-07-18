package com.lyf.scm.core.api.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批量导入需求单参数
 *
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Data
public class DemandBatchDTO {

    @ApiModelProperty("渠道编码")
    private String channelCode;

    @ApiModelProperty("包装类型")
    private String packType;

    @ApiModelProperty("包装仓库")
    private String packWarehouse;

    @ApiModelProperty("领料仓库")
    private String pickWarehouse;

    @ApiModelProperty("需求日期")
    private Date demandDate;

    @ApiModelProperty("优先级")
    private Integer priority;

    @ApiModelProperty("需求部门")
    private String department;

    @ApiModelProperty("需求提出人")
    private String introducer;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("商品编号")
    private String skuCode;

    @ApiModelProperty("商品Id")
    private Long skuId;

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("需求数量")
    private BigDecimal requireQty;

    @ApiModelProperty("单位")
    private String unitCode;

    @ApiModelProperty("自定义组合码（自定义组合必填）")
    private String customGroupCode;

    @ApiModelProperty("组合份数（自定义组合必填）")
    private BigDecimal compositeQty;

    @ApiModelProperty("明细备注")
    private String detailRemark;

    private Integer row;
}
