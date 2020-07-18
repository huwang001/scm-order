package com.lyf.scm.core.api.dto.order;

import lombok.Data;

import java.util.Date;

/**
 * 预约单DTO对象
 *
 * @author zhangxu
 * @date 2020/4/8
 */
@Data
public class OrderDTO {

    /** 主键 */
    private Long id;

    private String parentOrderCode;

    /** 预约单号 */
    private String orderCode;

    /** 单据类型 1:普通订单 2:卡券订单 */
    private Integer orderType;

    /** 单据状态 */
    private Integer orderStatus;

    /** 渠道编号 */
    private String channelCode;

    /** 销售单号 */
    private String saleCode;

    /** 客户名称 */
    private String customName;

    /** 客户手机号 */
    private String customMobile;

    /** 省编号 */
    private String provinceCode;

    /** 省名称 */
    private String provinceName;

    /** 市编号 */
    private String cityCode;

    /** 市名称 */
    private String cityName;

    /** 区编号 */
    private String countyCode;

    /** 区名称 */
    private String countyName;

    /** 客户详细地址 */
    private String customAddress;

    /** 是否需要包装 0不需要 1需要 */
    private Integer needPackage;

    /** 包装类型 1:组合 2:组装 */
    private Integer packageType;

    /** 预计交货日期 */
    private Date expectDate;

    private String factoryCode;

    private String realWarehouseCode;

    private String vmWarehouseCode;

    private Integer hasTradeAudit;

    private Integer hasAllot;

    private String allotCode;

    private String allotFactoryCode;

    private String allotRealWarehouseCode;

    private Integer hasDo;

    private String doCode;

    private String doFactoryCode;

    private String doRealWarehouseCode;

    private Integer isLeaf;

    private Integer syncTradeStatus;
    
    /** 包装份数 */
    private Integer packageNum;
    
    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人 */
    private Long creator;

    /** 更新人 */
    private Long modifier;

    private Long tenantId;

    private String appId;
}
