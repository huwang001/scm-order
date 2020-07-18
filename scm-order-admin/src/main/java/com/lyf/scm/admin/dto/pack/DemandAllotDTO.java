package com.lyf.scm.admin.dto.pack;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 需求调拨传输对象
 * <p>
 * @Author: wwh 2020/7/7
 */
@Data
public class DemandAllotDTO {
	
	 @ApiModelProperty(value = "需求单号")
	 @NotBlank(message = "需求单号不能为空")
	 private String requireCode;
	 
	 @ApiModelProperty(value = "用户ID")
	 private Long userId;
	 
	 @ApiModelProperty(value = "需求调拨明细")
	 @NotNull(message="需求调拨明细不能为空")
	 @Valid
	 private List<DemandAllotDetailDTO> demandAllotDetailList;

}