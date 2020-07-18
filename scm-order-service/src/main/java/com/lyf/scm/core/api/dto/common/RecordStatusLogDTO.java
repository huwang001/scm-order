package com.lyf.scm.core.api.dto.common;

import java.io.Serializable;

import com.lyf.scm.core.api.dto.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 单据状态流转日志传输对象
 * <p>
 * @Author: wwh 2020/3/5
 */
@Data
public class RecordStatusLogDTO extends BaseDTO implements Serializable {
	
	@ApiModelProperty(value = "需求单号")
	private String recordCode;
	
	@ApiModelProperty(value = "sap采购单号")
	private String sapOrderCode;
	
	@ApiModelProperty(value = "单据流转状态 1已同步 10已提交 20已确认 30待发货 40已发货 50已收货 60已取消")
	private Integer recordStatus;

}