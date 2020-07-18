package com.lyf.scm.core.api.dto.pack;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PackTaskOperationDTO implements Serializable {

    @ApiModelProperty(value = "需求单号")
    @NotNull(message = "需求单号不能为空")
    private String requireCode;

    @ApiModelProperty(value = "任务单号")
    @NotNull(message = "任务单号不能为空")
    private String taskCode;

    @ApiModelProperty(value = "任务操作单号")
    @NotNull(message = "任务操作单号不能为空")
    private String taskDetailOperateCode;

    @ApiModelProperty(value = "出向仓库编码-内部编码")
    @NotNull(message = "出向仓库编码不能为空")
    private String warehouseCode;

    @ApiModelProperty(value = "包装组线")
    @NotNull(message = "包装组线不能为空")
    private String packLine;

    @ApiModelProperty(value = "商品编码")
    @NotNull(message = "商品编码不能为空")
    private String skuCode;

    @ApiModelProperty(value = "实际包装日期")
    @NotNull(message = "实际包装日期不能为空")
    private Date packTime;

    @ApiModelProperty(value = "包装数量")
    @NotNull(message = "包装数量不能为空")
    private BigDecimal packNum;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "操作用户ID不能为空")
    private Long userId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
