package com.lyf.scm.admin.dto.pack;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

@Data
public class PackDemandExcelDTO {

    @ExcelProperty("包装需求单号")
    @ColumnWidth(20)
    private String recordCode;

    @ExcelProperty("销售单号")
    @ColumnWidth(30)
    private String saleCode;

    @ExcelProperty("外部单号")
    @ColumnWidth(15)
    private String outCode;

    @ExcelProperty("客户")
    @ColumnWidth(10)
    private String introducer;

    /**
     * 包装类型 1:组装2:反拆3:自定义组合4:自定义反拆5:拆箱
     */
    @ExcelIgnore
    private Integer packType;

    @ExcelProperty("包装类型")
    @ColumnWidth(13)
    private String packTypeDesc;

    @ExcelProperty("渠道")
    @ColumnWidth(15)
    private String channelCode;

    @ExcelProperty("渠道名称")
    @ColumnWidth(60)
    private String channelName;

    @ExcelProperty("需求部门")
    @ColumnWidth(20)
    private String department;

    @ExcelProperty("需求日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(13)
    private Date demandDate;

    @ExcelProperty("包装车间")
    @ColumnWidth(13)
    private String packWorkshopCode;

    @ExcelProperty("领料仓库")
    @ColumnWidth(13)
    private String pickWorkshopCode;

    @ExcelProperty("备注")
    @ColumnWidth(20)
    private String remark;

    /**
     * 单据状态 0:初始1:已确认2:已计划3:部分包装4:已包装完成
     */
    @ExcelIgnore
    private Integer recordStatus;

    @ExcelProperty("单据状态")
    @ColumnWidth(12)
    private String recordStatusDesc;

    /**
     * 领料状态 0:未领料1:领料中2:已部分领料3:已全部领料
     */
    @ExcelIgnore
    private Integer pickStatus;

    @ExcelProperty("领料状态")
    @ColumnWidth(12)
    private String pickStatusDesc;

    /**
     * 创建方式 1：接口创建2：页面创建3：导入创建
     */
    @ExcelIgnore
    private Integer createType;

    @ExcelProperty("创建方式")
    @ColumnWidth(12)
    private String createTypeDesc;

    @ExcelProperty("创建人")
    @ColumnWidth(10)
    private Long creator;

    @ExcelProperty("创建时间")
    @ColumnWidth(20)
    private Date createTime;

    @ExcelProperty("更新人")
    @ColumnWidth(10)
    private Long modifier;

    @ExcelProperty("更新时间")
    @ColumnWidth(20)
    private Date updateTime;
}
