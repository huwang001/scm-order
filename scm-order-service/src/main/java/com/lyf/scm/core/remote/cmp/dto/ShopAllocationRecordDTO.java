package com.lyf.scm.core.remote.cmp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 调拨前置单据
 * @date 2020/6/17 14:48
 * @Version
 */
@Data
@EqualsAndHashCode
public class ShopAllocationRecordDTO implements Serializable {

    private static final long serialVersionUID = 8413397819554852385L;
    /**
     * 调拨单号
     */
    private String recordCode;

    /**
     * 门店编号
     */
    private String shopCode;

    /**
     * 调拨时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date outCreateTime;

    /**
     * 商品编号
     */
    private String skuCode;
    /**
     * 商品数量
     */
    private BigDecimal skuQty;
    /**
     * 行id
     */
    private Long detailId;

    /**
     * 单位
     */
    private String unitCode;
}
