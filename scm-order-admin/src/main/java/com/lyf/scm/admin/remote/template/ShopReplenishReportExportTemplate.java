package com.lyf.scm.admin.remote.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 寻源结果导出
 * @date 2020/6/19
 */
@Data
@ColumnWidth(25)
public class ShopReplenishReportExportTemplate {
    @ExcelProperty("商品编号")
    private String skuCode;
    @ExcelProperty("商品名称")
    private String skuName;
    @ExcelProperty("工厂编号")
    private String factoryCode;
    @ExcelProperty("工厂名称")
    private String factoryName;
    @ExcelProperty("仓库编号")
    private String outRealWarehoseCode;
    @ExcelProperty("仓库名称")
    private String outRealWarehouseName;
    @ExcelProperty("门店编号")
    private String inShopCode;
    @ExcelProperty("前置单号")
    private String recordCode;
    @ExcelProperty("SAP采购单号")
    private String sapPoNo;
    @ExcelProperty("SAP交货单号")
    private String sapOrderCode;
    @ExcelProperty("需求数量")
    private BigDecimal skuQty;
    @ExcelProperty("寻源数量")
    private BigDecimal allotQty;
    @ExcelProperty("实际出库数量")
    private BigDecimal realOutQty;
    @ExcelProperty("实际入库数量")
    private BigDecimal realInQty;
    @ExcelProperty("PO行号")
    private String lineNo;
}
