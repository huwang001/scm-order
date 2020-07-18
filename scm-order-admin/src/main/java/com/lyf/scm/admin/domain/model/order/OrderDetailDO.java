package com.lyf.scm.admin.domain.model.order;

import com.lyf.scm.admin.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预约单明细数据对象
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@Data
public class OrderDetailDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 预约单号
     */
    private String orderCode;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 下单数量
     */
    private BigDecimal orderQty;

    /**
     * 需求锁定数量（下单数量按发货单位向上取整）
     */
    private BigDecimal requireQty;

    /**
     * 已锁定数量
     */
    private BigDecimal hasLockQty;

    /**
     * 发货单位
     */
    private String deliveryUnitCode;

    /**
     * 发货与基础转换比例
     */
    private BigDecimal scale;

    /**
     * 单位名称
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 锁定状态 1:部分锁定 2:全部锁定
     */
    private Integer lockStatus;

    /**
     * 商品名称
     */
    private String skuName;


    /**
     * 仓库数量
     */
    private BigDecimal wareHouseQty;

    /**
     * 实际发货数量
     */
    private BigDecimal deliverQty;


    /**
     * 调拨数量
     */
    private BigDecimal allotQty;


    /**allot
     *  发货仓库
     */
    private String deliverWareName;


}
