package com.lyf.scm.core.api.dto.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.lyf.scm.core.api.dto.stockFront.RwBatchDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 库存回调通知
 * <p>
 * @Author: chuwenchao  2020/6/19
 */
@Data
public class StockNotifyDTO implements Serializable {

    @ApiModelProperty(value = "单据编码")
    @NotBlank(message = "单据编码不能为空")
    private String recordCode;

    @ApiModelProperty(value = "wms单号（用作唯一请求识别）")
    @NotBlank(message = "wms单号不能为空")
    private String wmsOutCode;

    @ApiModelProperty(value = "订单完成时间")
    @NotBlank(message = "wms出入库时间不能为空")
    private String operateTime;

    @ApiModelProperty(value = "单据明细")
    @Valid
    @NotEmpty(message = "单据明细不能为空")
    private List<StockNotifyDetailDTO> detailDTOList;
    
}
