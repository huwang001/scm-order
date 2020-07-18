package com.lyf.scm.core.remote.item.dto;

import lombok.Data;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 18:59
 * @Version
 */
@Data
public class StoreSaleParamDTO {
    /**
     * 渠道编码
     */
    private String  channelCode;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * 单位编码
     */
    private String saleUnitCode;
}
