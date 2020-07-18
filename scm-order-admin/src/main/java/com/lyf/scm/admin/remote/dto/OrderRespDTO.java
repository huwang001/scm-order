package com.lyf.scm.admin.remote.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 接口返回预约单对象
 *
 * @author zhangxu
 */
@Data
public class OrderRespDTO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("父预约单号")
    private String parentOrderCode;

    @ApiModelProperty("预约单号")
    private String orderCode;

    @ApiModelProperty("单据类型 1:普通订单 2:卡券订单")
    private Integer orderType;

    @ApiModelProperty("单据状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消")
    private Integer orderStatus;

    @ApiModelProperty("渠道code")
    private String channelCode;

    @ApiModelProperty("销售单号")
    private String saleCode;

    @ApiModelProperty("客户名称")
    private String customName;

    @ApiModelProperty("客户手机号")
    private String customMobile;

    @ApiModelProperty("省Code")
    private String provinceCode;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市code")
    private String cityCode;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区code")
    private String countyCode;

    @ApiModelProperty("区名称")
    private String countyName;

    @ApiModelProperty("客户详细地址")
    private String customAddress;

    @ApiModelProperty("是否需要包装 0:不需要 1:需要")
    private Integer needPackage;

    @ApiModelProperty("预计交货日期")
    private Date expectDate;

    private String expectDateStr;


    @ApiModelProperty("工厂编号")
    private String factoryCode;

    @ApiModelProperty("实仓Code")
    private String realWarehouseCode;

    @ApiModelProperty("实仓名称")
    private String realWarehouseName;

    @ApiModelProperty("虚仓Code")
    private String vmWarehouseCode;

    private String vmWarehouseName;

    @ApiModelProperty("交易是否审核通过 0:未通过 1:已通过")
    private Integer hasTradeAudit;

    @ApiModelProperty("是否创建调拨 0:未创建 1:已创建")
    private Integer hasAllot;

    @ApiModelProperty("调拨单号")
    private String allotCode;

    @ApiModelProperty("调入工厂编码")
    private String allotFactoryCode;

    @ApiModelProperty("调入实仓code")
    private String allotRealWarehouseCode;

    @ApiModelProperty("是否创建DO 0:未创建 1:已创建")
    private Integer hasDo;

    @ApiModelProperty("团购发货单号")
    private String doCode;

    @ApiModelProperty("do发货工厂")
    private String doFactoryCode;

    @ApiModelProperty("do发货工厂名称")
    private String doFactoryName;

    @ApiModelProperty("do发货仓库编码")
    private String doRealWarehouseCode;

    @ApiModelProperty("是否叶子单, 0:否 1:是")
    private Integer isLeaf;

    @ApiModelProperty("同步交易状态 0:无需同步 1:待同步(锁定) 2:已同步(锁定) 10:待同步(DO) 11:已同步(DO)")
    private Integer syncTradeStatus;
    
    @ApiModelProperty(value = "包装份数")
    private Integer packageNum;
    
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    private String createTimeStr;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建人")
    private Long creator;

    @ApiModelProperty("更新人")
    private Long modifier;

    private List<OrderDetailRespDTO> orderDetailRespDTOS;
}
