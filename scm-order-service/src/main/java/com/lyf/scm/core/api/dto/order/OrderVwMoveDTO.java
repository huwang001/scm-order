package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 虚仓转移记录DTO
 * <p>
 * @Author: chuwenchao  2020/4/9
 */
@Data
public class OrderVwMoveDTO {

    @ApiModelProperty("预约单号")
    @NotBlank(message = "预约单号不能为空")
    private String orderCode;

    @ApiModelProperty("工厂编号")
    private String factoryCode;

    @ApiModelProperty("实仓code")
    private String realWarehouseCode;

    @ApiModelProperty("调入虚仓code")
    private String inVwWarehouseCode;

    @ApiModelProperty("调出虚仓code")
    @NotBlank(message = "调出虚仓code不能为空")
    private String outVwWarehouseCode;

    @ApiModelProperty("虚仓调拨明细")
    @Valid
    @NotEmpty(message = "虚仓调拨明细不能为空")
    private List<OrderVwMoveDetailDTO> detailDTOList;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 更新人
     */
    private Long modifier;
}
