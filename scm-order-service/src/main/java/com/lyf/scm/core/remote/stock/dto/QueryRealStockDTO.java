package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryRealStockDTO {

    /**
     * sku信息
     */
    private List<BaseSkuInfoDTO> baseSkuInfoDTOS;

    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 仓库外部编码
     */
    private String warehouseOutCode;
}
