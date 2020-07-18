package com.lyf.scm.core.service.dto;

import lombok.Data;

/**
 * 出库单 送达方信息
 */
@Data
public class RecordReceiveDTO {

    /**
     * 入库工厂编码
     */
    private String inFactoryCode;

    /**
     * 入库仓库编码
     */
    private String inRealWarehouseCode;

    /**
     * 店铺id
     */
    private String shopCode;

    /**
     * 调拨类型 1.内部调拨 2.RDC调拨
     */
    private Integer allocationType;
}
