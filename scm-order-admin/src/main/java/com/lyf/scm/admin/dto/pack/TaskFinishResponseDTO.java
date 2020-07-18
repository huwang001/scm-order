package com.lyf.scm.admin.dto.pack;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TaskFinishResponseDTO {

    @ApiModelProperty(value = "任务完成清单ID")
    private Long id;

    @ApiModelProperty(value = "包装需求单号")
    private String requireCode;

    @ApiModelProperty(value = "包装任务单号")
    private String taskCode;

    @ApiModelProperty(value = "包装任务明细操作单号")
    private String taskDetailOperateCode;

    @ApiModelProperty(value = "完成清单编码-前置单号")
    private String recordCode;

    @ApiModelProperty(value = "包装组线")
    private String packLine;

    @ApiModelProperty(value = "包装类型")
    private Integer packType;

    @ApiModelProperty(value = "包装类型描述")
    private String packTypeDesc;

    @ApiModelProperty(value = "渠道")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "成品商品编码")
    private String skuCode;

    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "实际包装日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date packTime;

    @ApiModelProperty(value = "计划包装数量")
    private BigDecimal planNum;

    @ApiModelProperty(value = "已包装数量")
    private BigDecimal packNum;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private Long modifier;

    @ApiModelProperty(value = "修改人工号")
    private String modifierNo;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
