package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 预约单查询对象
 *
 * @author zhangxu
 * @date 2020/4/13
 */
@Data
public class QueryOrderDTO {

    @ApiModelProperty(value = "预约单号")
    private String orderCode;

    @ApiModelProperty(value = "销售单号")
    private String saleCode;

    @ApiModelProperty(value = "预约单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "所属客户（客户名称或者客户手机）")
    private String custom;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 单据类型 1:普通订单 2:卡券订单
     */
    private Integer orderType;
}
