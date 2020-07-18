package com.lyf.scm.core.remote.transport.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 同步派车系统header对象
 */
@Data
public class DoMainDTO {

    /**
     * 出库单号
     */
    private String doNo;

    /**
     * 出库单类型
     */
    private Integer doType;

    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 工厂名称
     */
    private String factoryName;

    /**
     * 送达方代码
     */
    private String receiverCode;

    /**
     * 送达方名称
     */
    private String receiverName;

    /**
     * 送达方类型 1:工厂 2:门店
     */
    private Integer receiverType;

    /**
     * 交货日期
     */
    private Date deliveryDate;

    /**
     * 仓库编码
     */
    private String deliveryWarehouseCode;

    /**
     * 仓库名称
     */
    private String deliveryWarehouseName;

    /**
     * 毛重
     */
    private BigDecimal roughWeight;

    /**
     * 体积
     */
    private BigDecimal volume;

    /**
     * 明细
     */
    private List<DoDetailDTO> doDetailDTOList;
}
