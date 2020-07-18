package com.lyf.scm.core.api.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 接口返回预约单明细对象
 *
 * @author zhangxu
 */
@Data
public class OrderDetailRespDTO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("预约单号")
    private String orderCode;

    @ApiModelProperty("商品编码")
    private String skuCode;

    @ApiModelProperty("商品名称")
    private String skuName;

    @ApiModelProperty("下单数量")
    private BigDecimal orderQty;

    @ApiModelProperty("需求锁定数量（下单数量按发货单位向上取整）")
    private BigDecimal requireQty;

    @ApiModelProperty("已锁定数量")
    private BigDecimal hasLockQty;

    @ApiModelProperty("发货单位")
    private String deliveryUnitCode;

    @ApiModelProperty("发货与基础转换比例")
    private BigDecimal scale;

    @ApiModelProperty("单位名称")
    private String unit;

    @ApiModelProperty("单位编码")
    private String unitCode;

    @ApiModelProperty("锁定状态 1:部分锁定 2:全部锁定")
    private Integer lockStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建人")
    private Long creator;

    @ApiModelProperty("更新人")
    private Long modifier;
}
