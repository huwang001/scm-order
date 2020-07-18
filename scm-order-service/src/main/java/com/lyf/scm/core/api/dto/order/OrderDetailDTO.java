package com.lyf.scm.core.api.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 预约单明细DTO对象
 *
 * @author zhangxu
 * @date 2020/4/8
 */
@Data
public class OrderDetailDTO {

    /** 主键 */
    private Long id;

    /** 预约单号 */
    private String orderCode;

    /** 商品编号 */
    private String skuCode;

    /** 下单数量 */
    private BigDecimal orderQty;

    /** 需求锁定数量（下单数量按发货单位向上取整） */
    private BigDecimal requireQty;

    /** 已锁定数量 */
    private BigDecimal hasLockQty;

    /** 发货单位 */
    private String deliveryUnitCode;

    /** 发货与基础转换比例 */
    private BigDecimal scale;

    /** 单位编号 */
    private String unitCode;

    /** 单位名称 */
    private String unit;

    /** 锁定状态 1:部分锁定 2:全部锁定 */
    private Integer lockStatus;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人 */
    private Long creator;

    /** 更新人 */
    private Long modifier;

    private Long tenantId;

    private String appId;
}
