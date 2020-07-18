package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName UpdateOrderDTO
 * @Description 更新预约单对象
 * @Author huwang
 * @Date 2020/7/1610:09
 **/

@Data
@ApiModel(value = "预约单对象")
public class UpdateOrderDTO {

    @ApiModelProperty(value = "唯一主键")
    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty(value = "单据状态")
    @NotNull(message = "单据状态不能为空")
    private Integer orderStatus;

    @ApiModelProperty(value = "原单据状态")
    @NotNull(message = "原单据状态不能为空")
    private Integer oldOrderStatus;

    @ApiModelProperty(value = "版本号")
    @NotNull(message = "版本号不能为空")
    private Integer versionNo;

    @ApiModelProperty(value = "工厂编码")
    @NotBlank(message = "工厂编码不能为空")
    private String factoryCode;

    @ApiModelProperty(value = "外部仓库编码")
    @NotBlank(message = "外部仓库编码不能为空")
    private String realWarehouseOutCode;

    @ApiModelProperty(value = "调入工厂编码")
    @NotBlank(message = "调入工厂编码不能为空")
    private String allotFactoryCode;

    @ApiModelProperty(value = "调入实仓编码")
    @NotBlank(message = "调入实仓编码不能为空")
    private String allotRealWarehouseCode;

    @ApiModelProperty(value = "虚拟仓库编码")
    @NotBlank(message = "虚拟仓库编码不能为空")
    private String virtualWarehouseCode;
}
