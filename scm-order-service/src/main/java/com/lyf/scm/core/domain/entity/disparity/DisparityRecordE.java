package com.lyf.scm.core.domain.entity.disparity;

import com.lyf.scm.core.domain.model.disparity.DisparityRecordDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: DisparityRecordE  
 * @Description: 差异订单实体E  
 * @author: Lin.Xu  
 * @date: 2020-7-10 20:13:00
 * @version: v1.0
 */
@Data
public class DisparityRecordE extends DisparityRecordDO {

	private static final long serialVersionUID = 5354669293337130988L;

	@ApiModelProperty(value = "调整库存的时间")
	private Date outCreateTime;


}
