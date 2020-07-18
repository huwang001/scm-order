package com.lyf.scm.core.api.dto.stockFront;

import com.lyf.scm.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode
public class ReplenishAllotLogDTO extends BaseDTO {

    @ApiModelProperty(value = "唯一主键")
    private Long id;

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
    /**
     * 寻源类型  1-指定单据寻源, 2-当日寻源
     */
    private String allotTypeName;

    @ApiModelProperty(value = "当日寻源次数")
    private Integer times;

    @ApiModelProperty(value = "寻源订单数")
    private Integer totalRecords;

    @ApiModelProperty(value = "寻源成功订单数")
    private Integer successRecords;

    @ApiModelProperty(value = "是否可用：0-否，1-是")
    private Integer isAvailable;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "更新人")
    private Long modifier;
}
