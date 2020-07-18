package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderCreatePackDemandDTO
 * @Description 创建需求单
 * @Author huwang
 * @Date 2020/7/1616:04
 **/

@Data
public class OrderCreatePackDemandDTO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("预约单号")
    private String orderCode;

    @ApiModelProperty(value = "领料仓编码")
    private String outFactoryCode;

    @ApiModelProperty("领料仓外部编码")
    private String outRealWarehouseCode;

    @ApiModelProperty("包装仓工厂编码")
    private String inFactoryCode;

    @ApiModelProperty("包装仓外部编码")
    private String inRealWarehouseCode;

}
