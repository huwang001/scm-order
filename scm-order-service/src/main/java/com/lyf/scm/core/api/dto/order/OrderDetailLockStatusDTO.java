package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * com.lyf.scm.core.api.dto
 *
 * @author zhangxu
 * @date 2020/4/15
 */
@Data
@ApiModel("预约单明细锁定状态对象")
public class OrderDetailLockStatusDTO {

    @ApiModelProperty("sku编号")
    private String skuCode;

    @ApiModelProperty("订购数量")
    private BigDecimal orderQty;

    @ApiModelProperty("需求锁定数量")
    private BigDecimal requireQty;

    @ApiModelProperty("锁定数量")
    private BigDecimal hasLockQty;

    @ApiModelProperty("锁定状态: 0:未锁定 1:部分锁定 2:全部锁定")
    private Integer lockStatus;
}
