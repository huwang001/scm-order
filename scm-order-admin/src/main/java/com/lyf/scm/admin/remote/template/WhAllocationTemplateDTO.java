package com.lyf.scm.admin.remote.template;

import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * 类WhAllocationTemplateDTO的实现描述：导入模板DTO
 *
 * @author sunyj 2019/7/19 14:23
 */
@Data
public class WhAllocationTemplateDTO {

	@ExcelProperty("*调拨类型")
	private String businessTypeStr;

	@ExcelProperty("*是否退货")
	private String isReturnAllotcate;

	@ExcelProperty("*是否质量调拨")
	private String isQualityAllotcate;

	@ExcelProperty("调拨日期(不填默认当天)")
	private Date allotTime;

	@ExcelProperty("*出库工厂编号")
	private String outFactoryCode;

	@ExcelProperty("*出库仓库")
	private String outRealWareCode;

	@ExcelProperty("调出联系人")
	private String outWarehouseName;

	@ExcelProperty("调出联系人电话")
	private String outWarehouseMobile;

	@ExcelProperty("*入库工厂编号")
	private String inFactoryCode;

	@ExcelProperty("*入库仓库")
	private String inRealWareCode;

	@ExcelProperty("调入联系人")
	private String inWarehouseName;

	@ExcelProperty("调入联系人电话")
	private String inWarehouseMobile;

	@ExcelProperty("预计到货日期(不填默认当天)")
	private Date expeAogTimeStr;
	
	@ExcelProperty("*商品编号")
    private String skuCode;

    @ExcelProperty("*数量")
    private String allotQty;

    @ExcelProperty("*单位编号")
    private String unitCode;

    @ExcelProperty("*退货原因(退货时必填)")
    private String returnReason;
    
    @ExcelIgnore
    private List<WhAllocationTemplateDetailDTO> frontRecordDetails;
    
}
