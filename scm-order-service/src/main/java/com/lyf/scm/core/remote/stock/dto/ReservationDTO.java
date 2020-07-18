package com.lyf.scm.core.remote.stock.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 预约单DTO对象
 */
@Data
public class ReservationDTO {

    /** 渠道编码 */
    private String channelCode;

    /** 外部单据号  */
    private String outRecordCode;

    /**  业务类型：1-DIY　２-普通订单 3--卡订单 */
    private Integer businessType;

    /** 手机号 */
    private String mobile;

    /** 用户编码 */
    private String userCode;

    /** 备注 */
    private String remark;

    /** 省 */
    private String province;

    /** 省编码 */
    private String provinceCode;

    /** 市 */
    private String city;

    /** 市编码 */
    private String cityCode;

    /** 区县 */
    private String county;

    /** 区县编码 */
    private String countyCode;

    /** 详细地址 */
    private String address;

    /** 收货人姓名  */
    private String name;

    /** 外部订单下单时间 */
    private String outCreateTime;

    private Date expectReceiveDateStart;

    private String recordCode;
    private String realWarehouseCode;
    private String realFactoryCode;
    private String virtualWarehouseCode;
    private String virtualFactoryCode;

    /** 单据类型 */
    private Integer recordType;

    /** 预约单明细  */
    private List<ReservationDetailDTO> reservationDetails;

    @Data
    public static class ReservationDetailDTO {
        /** sku编码 */
        private String skuCode;

        /** 商品数量 */
        private BigDecimal skuQty;

        /** 单位编码 */
        private String unitCode;

        /** 单位 */
        private String unit;

        /** 商品skuId */
        private Long skuId;

        /** 已锁定数量 */
        private BigDecimal assignedQty;

        /** 未锁定数量 */
        private BigDecimal unassignedQty;

        private String recordCode;
    }
}
