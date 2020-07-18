package com.lyf.scm.core.api.dto.orderReturn;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 接收库存中心退货入库通知传输对象
 * <p>
 * @Author: wwh 2020/4/10
 */
@Data
public class ReturnNoticeDTO implements Serializable {
	
	@ApiModelProperty(value = "销售单号")
    private String saleCode;

	@ApiModelProperty(value = "售后单号")
	@NotBlank(message="售后单号不能为空")
    private String afterSaleCode;

    
    @ApiModelProperty(value = "退货入库通知详情")
    @NotNull(message="退货入库通知不能为空")
    @Valid
    private List<ReturnDetailNoticeDTO> returnDetailNoticeList;
    
}