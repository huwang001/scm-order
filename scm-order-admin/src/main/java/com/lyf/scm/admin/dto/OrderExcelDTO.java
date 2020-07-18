package com.lyf.scm.admin.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderExcelDTO {

    @ExcelProperty("预约单号")
    private String orderCode;

    @ExcelProperty("销售单号")
    private String saleCode;

    @ExcelProperty("仓库调拨单号")
    private String allotCode;

    @ExcelProperty("团购发货单号")
    private String doCode;

    @ExcelProperty("所属客户")
    private String customName;


    @ExcelProperty("客户手机号")
    private String customMobile;

    @ExcelProperty("客户详细地址")
    private String customAddress;


    @ExcelProperty("销售订单类型")
    private String orderTypeStr;

    @ExcelProperty("预约单状态")
    private String orderStatusStr;


    @ExcelProperty("发货仓")
    private String realWarehouseName;

    @ExcelProperty("预计交货日期")
    private Date expectDate;
    
    @ExcelProperty("包装份数")
    private Integer packageNum;
    
    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private Date createTime;

//    @ExcelProperty("创建人")
//    private Long creator;

}
