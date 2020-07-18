package com.lyf.scm.admin.dto;

import com.lyf.scm.common.model.Pagination;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
public class SaleWarehouseRecordCondition extends Pagination {

    @ApiModelProperty(value = "出库单编号")
    private String recordCode;
    @ApiModelProperty(value = "单据业务类型：比如门店销售")
    private Integer recordType;
    @ApiModelProperty(value = "用户账号")
    private String userCode;
    @ApiModelProperty(value = "实仓id")
    private Long realWarehouseId;
    @ApiModelProperty(value = "实仓Code")
    private String realWarehouseCode;
    @ApiModelProperty(value = "单据状态")
    private Integer recordStatus;
    @ApiModelProperty(value = "出库单创建开始时间")
    private Date startTime;
    @ApiModelProperty(value = "出库单创建结束时间")
    private Date endTime;
    @ApiModelProperty(value = "出库单支付开始时间")
    private Date startPayTime;
    @ApiModelProperty(value = "出库单支付结束时间")
    private Date endPayTime;
    @ApiModelProperty(value = "渠道")
    private String channelCodes;
    @ApiModelProperty(value = "订单包含的skuid过滤条件")
    private Long skuId;
    @ApiModelProperty(value = "订单包含的skucode过滤条件")
    private String skuCode;
    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    private List<String> channelCodeList;
    @ApiModelProperty(value = "根据订单编号或skuId查询出来的符合id")
    private List<Long> warehouseRecordIds;
    @ApiModelProperty(value = "同步捋单系统状态 0-无需同步 1-待同步交货信息 2-已同步")
    private Integer syncFulfillmentStatus;
    @ApiModelProperty(value = "出入库单同步WMS状态：0-无需同步 1-未同步 2-已同步")
    private Integer syncWmsStatus;
}
