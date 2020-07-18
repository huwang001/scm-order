/**
 * Filename CoreRealWarehouseStockDo.java
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
 * redis中获取到的实仓库存
 * @author xly
 * @since 2019年4月19日 下午5:21:46
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class CoreRealWarehouseStockDTO {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 实仓ID
     */
    private Long realWarehouseId;

    /**
     * 商品sku编码
     */
    private Long skuId;

    /**
     * 真实库存
     */
    private BigDecimal realQty;

    /**
     * 锁定库存
     */
    private BigDecimal lockQty;

    /**
     * 在途库存
     */
    private BigDecimal onroadQty;

    /**
     * 质检库存
     */
    private BigDecimal qualityQty;

    /**
     * 不合格库存，注：一般是质检不合格库存
     */
    private BigDecimal unqualifiedQty;

    /**
     * 可用库存
     */
    private BigDecimal availableQty;
}
