package com.lyf.scm.core.remote.stock.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description: wms单据修改对象
 * <p>
 * @Author: chuwenchao  2019/10/18
 */
@Data
public class RecordOrder {

    /**
     * 中台单据单号
     */
    private String deliveryOrderCode;

    /**
     * 货主编码
     */
    private String ownerCode;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 派车单号
     */
    private String expressCode;

    /**
     * 单据明细
     */
    private List<OrderLine> orderLines;
}
