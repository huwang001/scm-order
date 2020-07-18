package com.lyf.scm.admin.remote.stockFront.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.lyf.scm.admin.remote.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WhAllocationRecordDTO类的实现描述：仓库调拨前置单据
 *
 * @author sunyj 2019/7/21 21:33
 */
@Data
public class WhAllocationDTO extends BaseDTO implements Serializable {

	@ApiModelProperty(value = "唯一主键")
	private Long id;

	@ApiModelProperty(value = "单据类型 22调拨单")
	private Integer recordType;

	@ApiModelProperty(value = "0已新建 1已确认 2已取消 5已派车 10已出库 11已入库")
	private Integer recordStatus;

	@ApiModelProperty(value = "单据编码")
	@NotBlank(message = "单据编码不能为空")
	private String recordCode;

	@ApiModelProperty(value = "1内部调拨 2RDC调拨 3退货调拨 4电商仓调拨 4质量问题调拨")
	@NotNull(message = "业务类型不能为空")
	private Integer businessType;
	
	@ApiModelProperty(value = "入向仓库id")
	@NotNull(message = "入向仓库不能为空")
	private Long inWarehouseId;

	@ApiModelProperty(value = "入向仓库编码")
	private String inRealWarehouseCode;

	@ApiModelProperty(value = "入向工厂编码")
	private String inFactoryCode;

	@ApiModelProperty(value = "入向仓库联系人")
	private String inWarehouseName;

	@ApiModelProperty(value = "入向联系电话")
	private String inWarehouseMobile;

	@ApiModelProperty(value = "出向仓库id")
	@NotNull(message = "出向仓库不能为空")
	private Long outWarehouseId;

	@ApiModelProperty(value = "出向仓库编码")
	private String outRealWarehouseCode;

	@ApiModelProperty(value = "出向工厂编码")
	private String outFactoryCode;

	@ApiModelProperty(value = "出向仓库联系人")
	private String outWarehouseName;

	@ApiModelProperty(value = "出向仓库电话")
	private String outWarehouseMobile;

	@ApiModelProperty(value = "调拨日期")
	@NotNull(message = "调拨日期不能为空")
	private Date allotTime;

	@ApiModelProperty(value = "预计到货日期")
	@NotNull(message = "预计到货日期不能为空")
	private Date expeAogTime;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "sku数量及明细")
	@NotNull(message = "sku数量及明细不能为空")
	@Valid
	private List<WhAllocationDetailDTO> frontRecordDetails;

	@ApiModelProperty(value = "创建人")
	private Long creator;

	@ApiModelProperty(value = "修改人")
	private Long modifier;

	@ApiModelProperty(value = "是否退货调拨 1是 0不是")
	private Integer isReturnAllotcate;

	@ApiModelProperty(value = "是否质量问题调拨 1是 0不是")
	private Integer isQualityAllotcate;

	@ApiModelProperty(value = "SAP下发状态 0无需下发 1待下发 2已下发")
	private Integer syncStatus;

}