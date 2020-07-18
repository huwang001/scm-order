package com.lyf.scm.core.remote.transport.dto;

import com.lyf.scm.core.api.dto.notify.RecordSkuUnitDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 同步派车系统detail对象
 */
@Data
public class DoDetailDTO {

    /**
     * 出库单号
     */
    private String doNo;


    /**
     * 商品编码
     */
    private String skuCode;

    /**
     * 计划商品数量
     */
    private BigDecimal skuQty;

    /**
     * 单位名称
     */
    private String unit;

    /**
     * 单位编码
     */
    private String unitCode;

    /**
     * 重量
     */
    private BigDecimal roughWeight;

    /**
     * 体积
     */
    private BigDecimal volume;

    /**
     * 商品单位List
     */
    private List<RecordSkuUnitDTO> skuUnitDTOList;
}
