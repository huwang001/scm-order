package com.lyf.scm.core.remote.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Desc:
 * @author:Huangyl
 * @date: 2020/7/7
 */
@Data
public class ObtainOrderDTO {

    @ApiModelProperty("需求单号")
    private String demandNo;
    @ApiModelProperty("客户")
    private String customerName;
    @ApiModelProperty("包装类型")
    private String packType;
    @ApiModelProperty("销售单号")
    private String saleCode;
    @ApiModelProperty("预约单号")
    private String orderCode;
    @ApiModelProperty("渠道编号")
    private String channelCode;
    @ApiModelProperty("渠道名称")
    private String channelName;
    @ApiModelProperty("需求完成日期--组合、反拆时需要")
    private Date finishDate;
    @ApiModelProperty("订购日期")
    private Date orderDate;
    @ApiModelProperty("仓库编码--组合、反拆时需要")
    private String warehouseCode;
    @ApiModelProperty("仓库名称--级合、反拆时需要")
    private String warehouseName;
    @ApiModelProperty("包装车间编码")
    private String packhouseCode;
    @ApiModelProperty("包装车间名称")
    private String packhouseName;
    @ApiModelProperty("领料状态")
    private String getStatus;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("创建人id")
    private Long creator;
    @ApiModelProperty("组装，拆箱时的成品列表信息")
    private List<ObtainDemandGoodsDTO> goodsList;
}
