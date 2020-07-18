package com.lyf.scm.core.remote.trade.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DoDTO {

    private String doNo;

    private List<DoItemDTO> doItemList;

    @Data
    public static class DoItemDTO {
        private Long skuId;
        private String skuCode;
        private BigDecimal skuQty;
    }
}
