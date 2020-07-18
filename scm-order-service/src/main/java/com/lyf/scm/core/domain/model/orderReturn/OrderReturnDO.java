package com.lyf.scm.core.domain.model.orderReturn;

import com.lyf.scm.core.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 退货单数据结构原始对象 <br>
 *
 * @Author wwh 2020/4/8
 */
@Data
public class OrderReturnDO extends BaseDO implements Serializable {
	
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 预约单号
     */
    private String orderCode;

    /**
     * 销售单号
     */
    private String saleCode;

    /**
     * 售后单号
     */
    private String afterSaleCode;

    /**
     * 团购入库单号
     */
    private String returnEntryCode;

    /**
     * 单据状态 1待入库 2已入库
     */
    private Integer orderStatus;

    /**
     * 客户名称
     */
    private String customName;

    /**
     * 客户手机号
     */
    private String customMobile;

    /**
     * 省Code
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区code
     */
    private String countyCode;

    /**
     * 区名称
     */
    private String countyName;

    /**
     * 客户详细地址
     */
    private String customAddress;

    /**
     * 工厂编码
     */
    private String factoryCode;

    /**
     * 退货实仓code
     */
    private String realWarehouseCode;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 通知交易中心状态 0初始 1待通知 2已通知
     */
    private Integer syncTradeStatus;

    /**
     * 通知库存中心状态 0初始 1待通知 2已通知
     */
    private Integer syncStockStatus;

    /**
     * 快递单号
     */
    private String expressNo;

    /**
     * 单据类型
     */
    private Integer recordType;


    /**
     * 单据编号
     */
    private String recordCode;

    /**
     * 渠道id
     */
    private String channelCode;

    /**
     * 外部系统数据创建时间（下单时间）
     */
    private Date outCreateTime;

}