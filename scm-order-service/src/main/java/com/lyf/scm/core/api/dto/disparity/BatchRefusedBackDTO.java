package com.lyf.scm.core.api.dto.disparity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**  
 * @ClassName: BatchRefusedMsgDTO  
 * @Description: 批量整单拒收返回信息  
 * @author: Lin.Xu  
 * @date: 2020-7-11 20:17:08
 * @version: v1.0
 */
@ApiModel(description = "批量整单拒收返回信息  ")
@Data
public class BatchRefusedBackDTO {
	
	@ApiModelProperty(name="recordCode",value = "入库订单号")
	private String recordCode;
	
	@ApiModelProperty(name="status",value = "取消状态")
	private Boolean status; 
	
}
