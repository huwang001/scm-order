package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

import java.util.List;

@Data
public class InWarehouseRecordDTO {

    /**
     * 渠道编码, 有就传,存储在后置单
     */
    private String channelCode;

    /**
     * 出库工厂code
     */
    private String factoryCode;

    /**
     * @Description 差异专用 差异单对应的叫货流程的出库单
     * @Author Lin.Xu
     * @Date 19:45 2020/7/17
     **/
    private String outWarehouseRecordCode;

    /**
     * 入库单据编码
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
     * 出库仓库code
     */
    private String warehouseCode;

    
    /**
     * 外部单据编码
     */
    private String outRecordCode;
    
    /**
     * 虚仓编号, 特定业务需要
     */
    private String virWarehouseCode;
    
    /**
     * sku数量及明细
     */
    private List<RecordDetailDTO> detailList;
    
}
