package com.lyf.scm.core.remote.item.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zys
 * @Description
 * @date 2020/6/15 18:50
 * @Version
 */
@Data
public class StorePurchaseParamDTO {

    /**
     * 门店编码
     */
    private String  storeCode;

    /**
     * sku编码集合
     */
    private List<String> skuCodes;
}
