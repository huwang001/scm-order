/**
 * Filename CoreVirtualLockStockDO.java
 * Company 上海来伊份科技有限公司。
 *
 * @author xly
 * @version
 */
package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 虚仓锁定库存
 *
 * @author xly
 * @since 2019年4月29日 下午7:50:52
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class CoreVirtualStockOpDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * sku编码
     */
    private Long skuId;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 真实库存数量
     */
    private BigDecimal realQty;

    /**
     * 锁定库存
     */
    private BigDecimal lockQty;

    /**
     * 解锁库存
     */
    private BigDecimal unlockQty;

    /**
     * 实仓id
     */
    private Long realWarehouseId;

    /**
     * 虚仓id
     */
    private Long virtualWarehouseId;

    /**
     * 目标，虚仓id
     */
    private Long toVirtualWarehouseId;

    /**
     * 商家id
     */
    private Long merchantId;

    /**
     * 虚拟仓库组id
     */
    private Long virtualWarehouseGroupId;

    /**
     * 单据编码
     */
    private String recordCode;

    /**
     * 库存交易类型
     */
    private Integer transType;

    /**
     * 渠道ID
     */
    private String channelCode;

    /**
     * 变化量
     */
    private BigDecimal changeQty;

    /**
     * 是否检查
     * 即减为负的时候不允许，还是允许
     * 当为true时，减为负的时候不允许---当为false时，减为负的时候允许的
     */
    private boolean checkBeforeOp = true;

    /**
     * 插入db的记录
     * 模型专用,主要是虚仓记录新增时用
     */
    private boolean insertDb = false;
}
