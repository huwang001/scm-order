package com.lyf.scm.admin.remote.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode
public class DisparityRecordExportTemplate {
    @ExcelProperty( "差异单号")
    private String recordCode;
    @ExcelProperty("sap单号")
    private String sapPoNo;
    @ExcelProperty("交货单号")
    private String sapDeliveryCode;
    @ExcelProperty("出库单号")
    private String outWarehouseRecordCode;
    @ExcelProperty( "入库单号")
    private String inWarehouseRecordCode;
    @ExcelProperty( "出库仓库编码")
    private String outRealWarehouseCode;
    @ExcelProperty( "出库仓库")
    private String outRealWarehouseName;
    @ExcelProperty( "入库仓库编码")
    private String inRealWarehouseCode;
    @ExcelProperty( "入库仓库")
    private String inRealWarehouseName;
    @ExcelProperty( "差异入库工厂编号")
    private String handlerInFactoryCode;
    @ExcelProperty("差异入库仓库编号")
    private String handlerInRealWarehouseCode;
    @ExcelProperty( "差异入库仓库")
    private String handlerInRealWarehouseName;
    @ExcelProperty( "单据状态")
    private String recordStatus;
    @ExcelProperty("商品编码")
    private String skuCode;
    @ExcelProperty( "商品名称")
    private String skuName;
    @ExcelProperty( "出库数量")
    private BigDecimal outSkuQty;
    @ExcelProperty("入库数量")
    private BigDecimal inSkuQty;
    @ExcelProperty("差异数量")
    private BigDecimal skuQty;
     @ExcelProperty("单位Code")
    private String unitCode;
     @ExcelProperty("单位")
    private String unit;
     @ExcelProperty("责任方")
    private String responsibleType;
     @ExcelProperty("责任原因")
    private String reasons;
     @ExcelProperty("备注")
    private String remark;
     @ExcelProperty("成本中心")
    private String costCenter;
     @ExcelProperty("创建时间" )
    private Date createTime;
     @ExcelProperty("更新人")
    private String employeeNumber;
     @ExcelProperty("更新时间")
    private Date updateTime;
}
