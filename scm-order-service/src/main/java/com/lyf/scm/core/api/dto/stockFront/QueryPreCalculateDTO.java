package com.lyf.scm.core.api.dto.stockFront;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询虚仓预计算
 * 
 * @author WWH
 *
 */
@Data
public class QueryPreCalculateDTO {
	
	@ApiModelProperty(value = "出库单单据编号")
	@NotBlank(message = "出库单单据编号不能为空")
	private String recordCode;

	@ApiModelProperty(value = "批次信息")
	@Valid
	@NotEmpty
	private List<RwBatchDTO> rwBatchs;
	
}