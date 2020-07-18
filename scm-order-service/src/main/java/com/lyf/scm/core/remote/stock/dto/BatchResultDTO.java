package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

/**  
 * @ClassName: BatchResultDTO  
 * @Description: 批量整单拒收返回信息   
 * @author: Lin.Xu  
 * @date: 2020-7-11 21:08:48
 * @version: v1.0
 */
@Data
public class BatchResultDTO {
	
	/** 入库订单号 */
	private String recordCode;
	/** 取消状态 */
	private Boolean status;

}
