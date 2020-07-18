package com.lyf.scm.admin.remote.template;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类WhAllocationExportTemplate的实现描述：调拨导出模板
 *
 * @author sunyj 2019/11/4 14:45
 */
@Data
public class WhAllocationExportTemplate {

    @ExcelProperty("调拨单号")
    private String recordCode;

    @ExcelProperty("调拨单状态")
    private String recordTypeStr;

    @ExcelProperty("SAP交货单号")
    private String sapOrderCode;

    @ExcelProperty("TMS派车单号")
    private String tmsRecordCode;

    @ExcelProperty("SAP同公司PO单号")
    private String sapPoNo;

    @ExcelProperty("调出工厂编号")
    private String outFactoryCode;

    @ExcelProperty("调出仓库编号")
    private String outWarehouseCode;

    @ExcelProperty("调入工厂编号")
    private String inFactoryCode;

    @ExcelProperty("调入仓库编号")
    private String inWarehouseCode;

    @ExcelProperty("调拨类型")
    private String businessTypeStr;

    @ExcelProperty("调拨日期")
    private Date allotTime;

    @ExcelProperty("创建日期")
    private Date createTime;

    @ExcelProperty("商品编号")
    private String skuCode;

    @ExcelProperty("商品名称")
    private String skuName;

    @ExcelProperty("初始调拨数量")
    private BigDecimal orginQty;

    @ExcelProperty("差异调拨数量")
    private BigDecimal diffQty;

    @ExcelProperty("调拨单位")
    private String unit;

    @ExcelProperty("调拨出库数量")
    private BigDecimal outQty;

    @ExcelProperty("调拨出库基本数量")
    private BigDecimal outBasicQty;

    @ExcelProperty("基本单位")
    private String basicUnit;

    @ExcelProperty("预下市信息")
    private String underCarriageStr;
    
}
