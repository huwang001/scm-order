package com.lyf.scm.admin.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class OrderDetailExcelDTO {

    @ExcelProperty("预约单号")
    private String orderCode;


    @ExcelProperty("调拨单号")
    private String allotCode;

    @ExcelProperty("发货单号")
    private String doCode;

    @ExcelProperty("所属客户")
    private String customName;

    @ExcelProperty("销售订单类型")
    private String orderTypeStr;
    //调拨单号、发货单号、所属客户、
    //销售订单类型

    @ExcelProperty("商品编码")
    private String skuCode;

    @ExcelProperty("商品名称")
    private String skuName;

    @ExcelProperty("基本单位")
    private String unit;

    @ExcelProperty("下单数量")
    private BigDecimal orderQty;
//仓库数量
    @ExcelProperty("需求锁定数量")
    private BigDecimal requireQty;

    @ExcelProperty("实际锁定数量")
    private BigDecimal hasLockQty;


    @ExcelProperty("实际发货数量")
    private BigDecimal deliveryQty;


    @ExcelProperty("预约单状态")
    private String orderStatusStr;

    @ExcelProperty("发货仓库")
    private String doFactoryName;

    @ExcelProperty("预计交货日期")
    private String expectDateStr;



    @ExcelProperty("创建时间")
    private String createTimeStr;
    //调拨数量、预约单状态、发货仓库、交货日期、创建时间

}
