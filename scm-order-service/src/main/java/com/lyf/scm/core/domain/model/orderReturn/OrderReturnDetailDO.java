package com.lyf.scm.core.domain.model.orderReturn;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 退货单详情数据结构原始对象 <br>
 *
 * @Author wwh 2020/4/8
 */
@Data
public class OrderReturnDetailDO extends BaseDO implements Serializable {
	
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 售后单号
     */
    private String afterSaleCode;

    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 退货数量
     */
    private BigDecimal returnQty;

    /**
     * 实际发货数量
     */
    private BigDecimal deliveryQty;

    /**
     * 实际收货数量
     */
    private BigDecimal entryQty;

    /**
     * 单位名称
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;

}