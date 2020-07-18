package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

import java.util.List;

@Data
public class OutWarehouseRecordDTO {

    /**
     * 渠道编码, 有就传,存储在后置单
     */
    private String channelCode;

    /**
     * 是否检查真实库存,目前涵盖场景:质量调拨
     */
    private Boolean checkRealStock;

    /**
     * sku数量及明细
     */
    private List<RecordDetailDTO> detailList;

    /**
     * 出库工厂code
     */
    private String factoryCode;

    /**
     * 虚仓分配数
     */
    private List<AllocationCalQtyRes> preResult;

    /**
     * 出库单据编码
     */
    private String recordCode;

    /**
     * 单据类型
     */
    private Integer recordType;

    /**
     * SAP的po单号
     */
    private String sapPoNo;

    /**
     * 虚仓编号, 特定业务需要
     */
    private String virWarehouseCode;

    /**
     * 出库仓库code
     */
    private String warehouseCode;
    
    /**
     * 外部单据编码
     */
    private String outRecordCode;

}
