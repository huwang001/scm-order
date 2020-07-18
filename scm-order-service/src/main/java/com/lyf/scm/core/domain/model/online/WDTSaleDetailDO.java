package com.lyf.scm.core.domain.model.online;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class WDTSaleDetailDO extends BaseDO {

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 所属单据编码
     */
    private String recordCode;
    /**
     * 单据id
     */
    private Long frontRecordId;
    /**
     * 商品sku编码
     */
    private Long skuId;
    /**
     * 商品sku编码
     */
    private String skuCode;
    /**
     * 数量
     */
    private BigDecimal skuQty;
    /**
     * 单位
     */
    private String unit;
    /**
     * 单位code
     */
    private String unitCode;

    /**
     * 拆单状态： 0待分拆到do单 1 已分拆到do单
     */
    private Integer splitStatus;

    private String lineNo;

    /**
     * 1赠品 2非赠品
     */
    private Integer giftType;

    /**
     * 所属主品的商品编码,为null则表示非组合品
     */
    private String parentSkuCode;


}

