package com.lyf.scm.admin.remote.dto;


import com.lyf.scm.common.model.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class ReplenishAllotLogCondition extends Pagination {
    @ApiModelProperty(value = "寻源类型  1-指定单据寻源, 2-当日寻源")
    private Integer allotType;
    @ApiModelProperty(value = "寻源地区")
    private String allotArea;
    @ApiModelProperty(value = "寻源开始执行时间查询范围")
    private Date startTime;
    @ApiModelProperty(value = "寻源开始执行时间查询范围")
    private Date endTime;
    @ApiModelProperty(value = "渠道编号")
    private String channelCode;
    @ApiModelProperty(value = "工厂编号")
    private String factoryCode;
}
