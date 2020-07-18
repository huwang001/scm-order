package com.lyf.scm.admin.dto.pack;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 批量导入需求单参数
 *
 * @Author: liuyao
 * @Date: 2020/7/7
 */
@Data
@ColumnWidth(20)
public class DemandBatchDTO {

    @ExcelProperty("*渠道Code")
    private String channelCode;

    @ExcelProperty("*包装类型")
    private String packType;

    @ExcelProperty("*包装仓库")
    private String packWarehouse;

    @ExcelProperty("*领料仓库")
    private String pickWarehouse;

    @ExcelProperty("*需求日期")
    private Date demandDate;

    @ExcelProperty("优先级")
    private Integer priority;

    @ExcelProperty("需求部门")
    private String department;

    @ExcelProperty("需求提出人")
    private String introducer;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("*商品编号")
    private String skuCode;

    @ExcelProperty("*需求数量")
    private BigDecimal requireQty;

    @ExcelProperty("*单位")
    private String unitCode;

    @ExcelProperty("自定义组合码（自定义组合必填）")
    private String customGroupCode;

    @ExcelProperty("组合份数（自定义组合必填）")
    private BigDecimal compositeQty;

    @ExcelProperty("明细备注")
    private String detailRemark;
    
}
