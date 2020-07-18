package com.lyf.scm.core.api.dto.online;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description 旺店通so单列表
 * @Author: liuyao
 * @Date: 2020/7/2
 */
@Data
@EqualsAndHashCode
public class WDTOrderPageInfoDTO {
    private  Long id;
    private Long merchantId;
    private String originOrderCode;
    private String outRecordCode;
    private String recordCode;

    private String channelCode;
    private String channelName;

    private Long realWarehouseId;
    private String realWarehouseCode;
    private String realWarehouseName;

    private Long virtualWarehouseId;
    private String virtualWarehouseCode;
    private String virtualWarehouseName;
    private String realWarehouseAddress;

    private Integer recordStatus;

    private Date payTime;

    private Date updateTime ;

    private Date createTime ;

    private Integer splitType;
    private Integer allotStatus;

    private String logisticsCode;

    private String provinceCode;
    private String cityCode;
    private String countyCode;
    private String province;
    private String city;
    private String county;
    private String address;
    private String name;
    private String mobile;
    private String addressSimply;
    /**
     * 页面操作的用户id，跟userCode 不是同一个东西
     */
    private Long userId;
    /**
     * 下单用户
     */
    private String userCode;

    private Integer detailSize;
    private BigDecimal totalQty;
    private List<WDTSaleDetailDTO> detailDTOList;
}
