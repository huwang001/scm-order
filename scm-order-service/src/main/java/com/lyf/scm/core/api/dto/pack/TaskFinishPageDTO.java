package com.lyf.scm.core.api.dto.pack;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskFinishPageDTO extends BaseDTO {

    @ApiModelProperty(value = "包装需求单号")
    private List<String> requireCodeList;

    @ApiModelProperty(value = "包装任务单号")
    private List<String> taskCodeList;

    @ApiModelProperty(value = "渠道编码")
    private List<String> channelCodeList;

    @ApiModelProperty(value = "包装车间Id")
    private Long realWarehouseId;

    @ApiModelProperty(value = "包装车间编码")
    private String warehouseCode;

    @ApiModelProperty(value = "包装类型")
    private Integer packType;

    @ApiModelProperty(value = "包含商品")
    private List<String> skuCodeList;

    @ApiModelProperty(value = "实际包装日期-开始")
    private Date packTimeStart;

    @ApiModelProperty(value = "实际包装日期-结束")
    private Date packTimeEnd;

    @ApiModelProperty(value = "创建时间-开始")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建时间-结束")
    private Date createTimeEnd;
}
