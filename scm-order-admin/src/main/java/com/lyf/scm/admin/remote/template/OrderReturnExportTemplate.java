package com.lyf.scm.admin.remote.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 退货单导出模板
 * <p>
 * @Author: wwh 2020/4/17
 */
@Data
@ColumnWidth(10)
public class OrderReturnExportTemplate {

    @ExcelProperty("售后单号")
    private String afterSaleCode;

    @ExcelProperty("预约单号")
    private String orderCode;

    @ExcelProperty("团购入库单号")
    private String returnEntryCode;

    @ExcelProperty("所属客户")
    private String customMobile;

    @ExcelProperty("单据状态")
    private String orderStatusDesc;

    @ExcelProperty("收货仓库")
    private String realWarehouseName;

    @ExcelProperty("退货原因")
    private String reason;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新时间")
    private Date updateTime;

}