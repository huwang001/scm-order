package com.lyf.scm.admin.domain.model.order;

import com.lyf.scm.admin.domain.model.common.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 预约单数据对象
 *
 * @author zhangxu
 * @date 2020/4/9
 */
@Data
public class OrderDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 父预约单号
     */
    private String parentOrderCode;

    /**
     * 预约单号
     */
    private String orderCode;

    /**
     * 单据类型 1:普通订单 2:卡券订单
     */
    private Integer orderType;

    /**
     * 单据状态 1:部分锁定 2:全部锁定 10:调拨审核通过 11:调拨出库 12:调拨入库/待加工 20:已加工 30:待发货 31:已发货 40:已取消
     */
    private Integer orderStatus;

    /**
     * 渠道code
     */
    private String channelCode;

    /**
     * 销售单号
     */
    private String saleCode;

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
     * 是否需要包装 0:不需要 1:需要
     */
    private Integer needPackage;

    /**
     * 包装类型 1:组合 2:组装
     */
    private Integer packageType;

    /**
     * 预计交货日期
     */
    private Date expectDate;

    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 实仓Code
     */
    private String realWarehouseCode;

    /**
     * 虚仓Code
     */
    private String vmWarehouseCode;

    /**
     * 交易是否审核通过 0:未通过 1:已通过
     */
    private Integer hasTradeAudit;

    /**
     * 是否创建调拨 0:未创建 1:已创建
     */
    private Integer hasAllot;

    /**
     * 调拨单号
     */
    private String allotCode;

    /**
     * 调入工厂编码
     */
    private String allotFactoryCode;

    /**
     * 调入实仓code
     */
    private String allotRealWarehouseCode;

    /**
     * 是否创建DO 0:未创建 1:已创建
     */
    private Integer hasDo;

    /**
     * 团购发货单号
     */
    private String doCode;

    /**
     * do发货工厂
     */
    private String doFactoryCode;

    /**
     * do发货仓库编码
     */
    private String doRealWarehouseCode;


    /**
     * do发货仓库名称
     */
    //todo  字段未确定
    private String doRealWarehouseName;


    /**
     * 是否叶子单, 0:否 1:是
     */
    private Integer isLeaf;

    /**
     * 同步交易状态 0:无需同步 1:待同步(锁定) 2:已同步(锁定) 10:待同步(DO) 11:已同步(DO)
     */
    private Integer syncTradeStatus;


    private List<OrderDetailDO> list ;


}
