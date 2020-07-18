package com.lyf.scm.admin.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 接口返回预约单明细对象
 *
 * @author zhangxu
 */
@Data
public class OrderDetailRespDTO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("调拨单号")
    private String allotCode;

    @ApiModelProperty("团购发货单号")
    private String doCode;

    @ApiModelProperty("客户名称")
    private String customName;

    @ApiModelProperty("单据类型 1:普通订单 2:卡券订单")
    private Integer orderType;
    //调拨单号、发货单号、所属客户、
    //销售订单类型

    @ApiModelProperty("预约单号")
    private String orderCode;

    @ApiModelProperty("商品编码")
    private String skuCode;

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("下单数量")
    private BigDecimal orderQty;

    @ApiModelProperty("需求锁定数量（下单数量按发货单位向上取整）")
    private BigDecimal requireQty;

    @ApiModelProperty("已锁定数量")
    private BigDecimal hasLockQty;

    @ApiModelProperty("发货单位")
    private String deliveryUnitCode;

    @ApiModelProperty("发货与基础转换比例")
    private BigDecimal scale;

    @ApiModelProperty("单位名称")
    private String unit;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("锁定状态 1:部分锁定 2:全部锁定")
    private Integer lockStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;

    private String createTimeStr;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建人")
    private Long creator;

    @ApiModelProperty("更新人")
    private Long modifier;


    @ApiModelProperty("单据状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消")
    private Integer orderStatus;


    @ApiModelProperty("do发货工厂名称")
    private String doFactoryName;

    @ApiModelProperty("预计交货日期")
    private Date expectDate;

    private String expectDateStr;



    //调拨数量、预约单状态、发货仓库、交货日期、创建时间
}
